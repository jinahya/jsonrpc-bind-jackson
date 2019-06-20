package com.github.jinahya.jsonrpc.bind.v2.calculator;

import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.JacksonServerResponseTest;

import java.math.BigDecimal;

public class CalculatorServerResponseTest
        extends JacksonServerResponseTest<CalculatorServerResponse, BigDecimal, CalculatorResponseError, ValueNode> {

    CalculatorServerResponseTest() {
        super(CalculatorServerResponse.class, BigDecimal.class, CalculatorResponseError.class, ValueNode.class);
    }
}
