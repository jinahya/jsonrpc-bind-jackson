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

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonUtils.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@Slf4j
class CalculatorControllerTest {

    // -----------------------------------------------------------------------------------------------------------------
    static Stream<Arguments> sourceRoundingModes() {
        return Arrays.stream(RoundingMode.values()).map(Arguments::of);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    private void onPostConstruct() {
        mockMvc = standaloneSetup(calculatorController).build();
        log.debug("mockMvc: {}", mockMvc);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void callAddNamed() throws Exception {
        final CalculatorRequestParams.AdditionParams params = new CalculatorRequestParams.AdditionParams();
        params.setAugend(BigDecimal.ZERO);
        params.setAddend(BigDecimal.ONE);
        final CalculatorClientRequestNamed calculatorRequest = new CalculatorClientRequestNamed();
        calculatorRequest.setMethod(CalculatorService.METHOD_ADD_NAMED);
        calculatorRequest.setParams(params);
        calculatorRequest.setId(System.nanoTime());
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    void callAddPositioned() throws Exception {
        final List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.ZERO);
        params.add(BigDecimal.ONE);
        final CalculatorClientRequestPositioned calculatorRequest = new CalculatorClientRequestPositioned();
        calculatorRequest.setMethod(CalculatorService.METHOD_ADD_POSITIONED);
        calculatorRequest.setParams(params);
        calculatorRequest.setId(System.nanoTime());
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void callForSubtraction() throws Exception {
        final CalculatorRequestParams.SubtractionParams params = new CalculatorRequestParams.SubtractionParams();
        params.setMinuend(BigDecimal.ONE);
        params.setSubtrahend(BigDecimal.ZERO);
        final CalculatorClientRequestNamed calculatorRequest = new CalculatorClientRequestNamed();
        calculatorRequest.setMethod(CalculatorService.METHOD_SUBTRACT_NAMED);
        calculatorRequest.setParams(params);
        calculatorRequest.setId(System.nanoTime());
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void callForMultiplication() throws Exception {
        final CalculatorRequestParams.MultiplicationParam params = new CalculatorRequestParams.MultiplicationParam();
        params.setMultiplicand(BigDecimal.ONE);
        params.setMultiplier(BigDecimal.ZERO);
        final CalculatorClientRequestNamed calculatorRequest = new CalculatorClientRequestNamed();
        calculatorRequest.setMethod(CalculatorService.METHOD_MULTIPLY_NAMED);
        calculatorRequest.setParams(params);
        calculatorRequest.setId(System.nanoTime());
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ZERO, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"sourceRoundingModes"})
    @ParameterizedTest
    void callForDivision(final RoundingMode roundingMode) throws Exception {
        final CalculatorRequestParams.DivisionParam params = new CalculatorRequestParams.DivisionParam();
        params.setDividend(BigDecimal.ONE);
        params.setDivisor(BigDecimal.ONE);
        params.setRoundingMode(roundingMode);
        requireValid(params);
        final CalculatorClientRequestNamed calculatorRequest = new CalculatorClientRequestNamed();
        calculatorRequest.setMethod(CalculatorService.METHOD_DIVIDE_NAMED);
        calculatorRequest.setParams(params);
        calculatorRequest.setId(System.nanoTime());
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @MethodSource({"sourceRoundingModes"})
    @ParameterizedTest
    void callForDivisionWithDivisorOfZero(final RoundingMode roundingMode) throws Exception {
        final CalculatorRequestParams.DivisionParam params = new CalculatorRequestParams.DivisionParam();
        params.setDividend(BigDecimal.ONE);
        params.setDivisor(BigDecimal.ZERO);
        params.setRoundingMode(roundingMode);
        requireValid(params);
        final CalculatorClientRequestNamed calculatorRequest = new CalculatorClientRequestNamed();
        calculatorRequest.setMethod(CalculatorService.METHOD_DIVIDE_NAMED);
        calculatorRequest.setParams(params);
        calculatorRequest.setId(System.nanoTime());
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/" + CalculatorController.PATH_VALUE_CALL)
                                 .contentType(APPLICATION_JSON).content(content)
                                 .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorClientResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorClientResponse.class);
        assertNull(calculatorResponse.getResult());
        final CalculatorResponseError error = calculatorResponse.getError();
        log.debug("error: {}", error);
        final long code = error.getCode();
        assertEquals(ResponseObject.ErrorObject.CODE_INVALID_REQUEST, code);
    }

    @Autowired
    private CalculatorController calculatorController;

    private transient MockMvc mockMvc;
}
