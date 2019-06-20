package com.github.jinahya.jsonrpc.bind.v2.calculator;

import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.JacksonServerRequestTest;

class CalculatorServerRequestTest extends JacksonServerRequestTest<CalculatorServerRequest, ValueNode> {

    CalculatorServerRequestTest() {
        super(CalculatorServerRequest.class, ValueNode.class);
    }
}
