package com.github.jinahya.jsonrpc.bind.v2.bind.jackson.calculator;

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

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static com.github.jinahya.jsonrpc.bind.v2.bind.JacksonUtils.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.v2.bind.jackson.calculator.CalculatorController.PATH_VALUE_CALL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@Slf4j
public class CalculatorControllerTest {

    @PostConstruct
    private void onPostConstruct() {
        mockMvc = standaloneSetup(calculatorController).build();
        log.debug("mockMvc: {}", mockMvc);
    }

    @Test
    public void testForAdd() throws Exception {
        final CalculatorRequestParams.AdditionParams params = new CalculatorRequestParams.AdditionParams();
        params.setAugend(BigDecimal.ZERO);
        params.setAddend(BigDecimal.ONE);
        final JsonNode paramsNode = OBJECT_MAPPER.valueToTree(params);
        final CalculatorRequest calculatorRequest = new CalculatorRequest();
        calculatorRequest.setMethod(CalculatorRequest.METHOD_ADD);
        calculatorRequest.setParams(paramsNode);
        calculatorRequest.setId(OBJECT_MAPPER.getNodeFactory().numberNode(System.nanoTime()));
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(post("/" + PATH_VALUE_CALL).contentType(APPLICATION_JSON).content(content)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    public void testForSubtract() throws Exception {
        final CalculatorRequestParams.SubtractionParams params = new CalculatorRequestParams.SubtractionParams();
        params.setMinuend(BigDecimal.ONE);
        params.setSubtrahend(BigDecimal.ZERO);
        final JsonNode paramsNode = OBJECT_MAPPER.valueToTree(params);
        final CalculatorRequest calculatorRequest = new CalculatorRequest();
        calculatorRequest.setMethod(CalculatorRequest.METHOD_SUBTRACT);
        calculatorRequest.setParams(paramsNode);
        calculatorRequest.setId(OBJECT_MAPPER.getNodeFactory().textNode("subtract1"));
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(post("/" + PATH_VALUE_CALL).contentType(APPLICATION_JSON).content(content)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    public void testForMultiply() throws Exception {
        final CalculatorRequestParams.MultiplicationParam params = new CalculatorRequestParams.MultiplicationParam();
        params.setMultiplicand(BigDecimal.ONE);
        params.setMultiplier(BigDecimal.ZERO);
        final JsonNode paramsNode = OBJECT_MAPPER.valueToTree(params);
        final CalculatorRequest calculatorRequest = new CalculatorRequest();
        calculatorRequest.setMethod(CalculatorRequest.METHOD_MULTIPLY);
        calculatorRequest.setParams(paramsNode);
        calculatorRequest.setId(OBJECT_MAPPER.getNodeFactory().numberNode(1.1d));
        final String content = OBJECT_MAPPER.writeValueAsString(calculatorRequest);
        final MvcResult mvcResult = mockMvc
                .perform(post("/" + PATH_VALUE_CALL).contentType(APPLICATION_JSON).content(content)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        final CalculatorResponse calculatorResponse = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(), CalculatorResponse.class);
        assertNull(calculatorResponse.getError());
        final BigDecimal result = calculatorResponse.getResult();
        assertEquals(BigDecimal.ZERO, result);
    }

    // TODO: 6/14/2019 add test case for division

    @Autowired
    private CalculatorController calculatorController;

    private transient MockMvc mockMvc;
}
