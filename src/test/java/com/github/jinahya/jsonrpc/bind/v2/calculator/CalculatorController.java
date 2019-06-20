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
import java.util.function.Function;

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
                    CalculatorResponseErrorData.of(null, null))));
        } catch (final ConstraintViolationException cve) {
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INVALID_REQUEST,
                    "not valid; " + cve.getConstraintViolations().iterator().next().getMessage(),
                    CalculatorResponseErrorData.of(null, null))));
        }
        final String requestMethod = calculatorRequest.getMethod();
        Method serviceMethod = null;
        Class<?> serviceParameterType = null;
        for (final Method declaredMethod : CalculatorService.class.getDeclaredMethods()) {
            final CalculatorProcedure calculatorProcedure
                    = declaredMethod.getAnnotation(CalculatorProcedure.class);
            if (calculatorProcedure == null) {
                log.info("not annotated with {}: {}", CalculatorProcedure.class, declaredMethod);
                continue;
            }
            String procedureMethod = calculatorProcedure.method();
            if (procedureMethod.isEmpty()) {
                procedureMethod = declaredMethod.getName();
            }
            if (!procedureMethod.equals(requestMethod)) {
                log.info("procedureMethod({}) <> requestMethod({})", procedureMethod, requestMethod);
                continue;
            }
            final Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            final Class<?> parameterType = parameterTypes[0];
            if (!CalculatorRequestParams.class.isAssignableFrom(parameterType)) {
                log.info("parameterType({}) is not assignable to {}", parameterType, CalculatorRequestParams.class);
                continue;
            }
            if (!declaredMethod.isAccessible()) {
                declaredMethod.setAccessible(true);
            }
            serviceMethod = declaredMethod;
            serviceParameterType = parameterTypes[0];
            break;
        }
        if (serviceMethod == null) {
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_METHOD_NOT_FOUND, "unknown method: " + requestMethod,
                    CalculatorResponseErrorData.of(calculatorRequest, null))));
        }
        final CalculatorRequestParams calculatorRequestParams = calculatorRequest.applyParams(
                OBJECT_MAPPER, serviceParameterType.asSubclass(CalculatorRequestParams.class), Function.identity());
        final BigDecimal result;
        try {
            result = (BigDecimal) serviceMethod.invoke(calculatorService, calculatorRequestParams);
        } catch (final InvocationTargetException ite) {
            final Throwable cause = ite.getCause();
            if (cause instanceof ArithmeticException) {
                final ArithmeticException arithmeticException = (ArithmeticException) cause;
                return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                        ErrorObject.CODE_INVALID_REQUEST, cause.getMessage(),
                        CalculatorResponseErrorData.of(calculatorRequest, arithmeticException))));
            }
            cause.printStackTrace();
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INTERNAL_ERROR, cause.getMessage(),
                    CalculatorResponseErrorData.of(calculatorRequest, null))));
        } catch (final Exception e) {
            return ResponseEntity.ok(CalculatorServerResponse.of(CalculatorResponseError.of(
                    ErrorObject.CODE_INTERNAL_ERROR, e.getMessage(),
                    CalculatorResponseErrorData.of(calculatorRequest, null))));
        }
        final CalculatorServerResponse calculatorResponse = CalculatorServerResponse.of(result);
        calculatorResponse.copyIdFrom(calculatorRequest);
        return ResponseEntity.ok(calculatorResponse);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private CalculatorService calculatorService;
}
