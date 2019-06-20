package com.github.jinahya.jsonrpc.bind.v2.calculator;

/*-
 * #%L
 * jsonrpc-bind
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.JsonParseException;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonUtils.OBJECT_MAPPER;

@RestController
@Slf4j
public class CalculatorController {

    public static final String PATH_NAME_CALL = "call";

    public static final String PATH_VALUE_CALL = "call";

    public static final String PATH_TEMPLATE_CALL = "{" + PATH_NAME_CALL + ":" + PATH_VALUE_CALL + "}";

    // -----------------------------------------------------------------------------------------------------------------
    @PostMapping(
            value = "/" + PATH_TEMPLATE_CALL,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<?> call(@NotNull final InputStream bodyStream) throws IOException {
        final CalculatorServerRequest calculatorRequest;
        try {
            calculatorRequest = requireValid(OBJECT_MAPPER.readValue(bodyStream, CalculatorServerRequest.class));
        } catch (final JsonParseException jpe) {
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_PARSE_ERROR, "failed to parse json; " + jpe.getMessage(),
                    CalculatorResponseErrorData.of(null, jpe))));
        } catch (final ConstraintViolationException cve) {
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INVALID_REQUEST,
                    "not valid; " + cve.getConstraintViolations().iterator().next().getMessage(),
                    CalculatorResponseErrorData.of(null, cve))));
        }
        final String requestMethod = calculatorRequest.getMethod();
        final Method serviceMethod
                = CalculatorService.findCalculatorMethod(ExtendedCalculatorService.class, requestMethod);
        log.debug("serviceMethod: {}", serviceMethod);
        if (serviceMethod == null) {
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INVALID_REQUEST, "method not found: " + requestMethod, null)));
        }
        if (!serviceMethod.isAccessible()) {
            serviceMethod.setAccessible(true);
        }
        final Class<?> serviceParameterType = serviceMethod.getParameterTypes()[0];
        final Object requestParams = calculatorRequest.getParams(OBJECT_MAPPER, serviceParameterType, BigDecimal.class);
        log.debug("requestParams: {}", requestParams);
        final BigDecimal result;
        try {
            result = (BigDecimal) serviceMethod.invoke(calculatorService, requestParams);
        } catch (final InvocationTargetException ite) {
            final Throwable cause = ite.getCause();
            if (cause instanceof ArithmeticException) {
                return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                        ErrorObject.CODE_INVALID_REQUEST, cause.getMessage(),
                        CalculatorResponseErrorData.of(calculatorRequest, cause))));
            }
            cause.printStackTrace();
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INTERNAL_ERROR, cause.getMessage(),
                    CalculatorResponseErrorData.of(calculatorRequest, cause))));
        } catch (final Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INTERNAL_ERROR, e.getMessage(),
                    CalculatorResponseErrorData.of(calculatorRequest, e))));
        }
        final CalculatorServerResponse calculatorResponse = CalculatorServerResponse.of(result);
        calculatorResponse.copyIdFrom(calculatorRequest);
        return ResponseEntity.ok(calculatorResponse);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private ExtendedCalculatorService calculatorService;
}
