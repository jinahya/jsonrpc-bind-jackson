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

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonUtils.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_ADD_NAMED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_ADD_POSITIONED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_DIVIDE_NAMED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_DIVIDE_POSITIONED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_MULTIPLY_NAMED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_MULTIPLY_POSITIONED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_SUBTRACT_NAMED;
import static com.github.jinahya.jsonrpc.bind.v2.calculator.CalculatorService.METHOD_SUBTRACT_POSITIONED;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@Slf4j
class CalculatorControllerTest {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Sources arguments for all values of {@link RoundingMode}.
     *
     * @return a stream of arguments which each is a value of {@link RoundingMode}.
     */
    static Stream<Arguments> sourceRoundingModes() {
        return Arrays.stream(RoundingMode.values()).map(Arguments::of);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    private void onPostConstruct() {
        mockMvc = standaloneSetup(calculatorController).build();
        log.debug("mockMvc: {}", mockMvc);
    }

    /**
     * Calls for {@link CalculatorController#call(InputStream)} with given request.
     *
     * @param request the request to send.
     * @return an instance of {@link CalculatorClientResponse}.
     * @throws Exception if an error occurs.
     */
    CalculatorClientResponse call(final CalculatorClientRequest<?> request) throws Exception {
        log.debug("request: {}", request);
        final long id = System.nanoTime();
        log.debug("id: {}", id);
        request.setId(id);
        final String content = OBJECT_MAPPER.writeValueAsString(requireValid(requireNonNull(request)));
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse response
                = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        log.debug("response: {}", response);
        ofNullable(response.getResult()).ifPresent(v -> assertEquals(request.getId(), response.getId()));
        ofNullable(response.getId()).ifPresent(v -> assertEquals(request.getId(), v));
        return response;
    }

    CalculatorClientResponse call(final String method, final CalculatorRequestParams params) throws Exception {
        final CalculatorClientRequest<CalculatorRequestParams> request = new CalculatorClientRequestNamed();
        request.setMethod(method);
        request.setParams(params);
        return call(request);
    }

    CalculatorClientResponse call(final String method, List<BigDecimal> params) throws Exception {
        final CalculatorClientRequest<List<BigDecimal>> request = new CalculatorClientRequestPositioned();
        request.setMethod(method);
        request.setParams(params);
        return call(request);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Calls for for {@value CalculatorService#METHOD_ADD_NAMED} method.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    void call_add_named() throws Exception {
        final CalculatorRequestParams.AdditionParams params = new CalculatorRequestParams.AdditionParams();
        params.setAugend(BigDecimal.ZERO);
        params.setAddend(BigDecimal.ONE);
        final CalculatorClientResponse response = call(METHOD_ADD_NAMED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    void call_add_positioned() throws Exception {
        final List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.ZERO);
        params.add(BigDecimal.ONE);
        final CalculatorClientResponse response = call(METHOD_ADD_POSITIONED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void call_subtract_named() throws Exception {
        final CalculatorRequestParams.SubtractionParams params = new CalculatorRequestParams.SubtractionParams();
        params.setMinuend(BigDecimal.ONE);
        params.setSubtrahend(BigDecimal.ZERO);
        final CalculatorClientResponse response = call(METHOD_SUBTRACT_NAMED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    void call_subtract_positioned() throws Exception {
        final List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.ONE);
        params.add(BigDecimal.ZERO);
        final CalculatorClientResponse response = call(METHOD_SUBTRACT_POSITIONED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void call_multiply_named() throws Exception {
        final CalculatorRequestParams.MultiplicationParam params = new CalculatorRequestParams.MultiplicationParam();
        params.setMultiplicand(BigDecimal.ONE);
        params.setMultiplier(BigDecimal.ZERO);
        final CalculatorClientResponse response = call(METHOD_MULTIPLY_NAMED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void call_multiply_positioned() throws Exception {
        final List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.ONE);
        params.add(BigDecimal.ZERO);
        final CalculatorClientResponse response = call(METHOD_MULTIPLY_POSITIONED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ZERO, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"sourceRoundingModes"})
    @ParameterizedTest
    void call_divide_named(final RoundingMode roundingMode) throws Exception {
        final CalculatorRequestParams.DivisionParam params = new CalculatorRequestParams.DivisionParam();
        params.setDividend(BigDecimal.ONE);
        params.setDivisor(BigDecimal.ONE);
        params.setRoundingMode(roundingMode);
        final CalculatorClientResponse response = call(METHOD_DIVIDE_NAMED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @MethodSource({"sourceRoundingModes"})
    @ParameterizedTest
    void call_divide_named_zero_divisor(final RoundingMode roundingMode) throws Exception {
        final CalculatorRequestParams.DivisionParam params = new CalculatorRequestParams.DivisionParam();
        params.setDividend(BigDecimal.ONE);
        params.setDivisor(BigDecimal.ZERO);
        params.setRoundingMode(roundingMode);
        final CalculatorClientResponse response = call(METHOD_DIVIDE_NAMED, params);
        final BigDecimal result = response.getResult();
        assertNull(result);
        final CalculatorResponseError error = response.getError();
        assertNotNull(error);
        final long code = error.getCode();
        assertEquals(ErrorObject.CODE_INVALID_REQUEST, code);
    }

    @Test
    void call_divide_positioned() throws Exception {
        final List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.ONE);
        params.add(BigDecimal.ONE);
        final CalculatorClientResponse response = call(METHOD_DIVIDE_POSITIONED, params);
        assertNull(response.getError());
        final BigDecimal result = response.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    void call_divide_positioned_zero_divisor() throws Exception {
        final List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.ONE);
        params.add(BigDecimal.ZERO);
        final CalculatorClientResponse response = call(METHOD_DIVIDE_POSITIONED, params);
        final BigDecimal result = response.getResult();
        assertNull(result);
        final CalculatorResponseError error = response.getError();
        assertNotNull(error);
        final long code = error.getCode();
        assertEquals(ErrorObject.CODE_INVALID_REQUEST, code);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private CalculatorController calculatorController;

    private transient MockMvc mockMvc;
}
