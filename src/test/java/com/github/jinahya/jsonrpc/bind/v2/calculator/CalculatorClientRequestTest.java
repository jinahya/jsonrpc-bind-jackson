package com.github.jinahya.jsonrpc.bind.v2.calculator;

import com.github.jinahya.jsonrpc.bind.v2.JacksonClientRequestTest;

class CalculatorClientRequestTest
        extends JacksonClientRequestTest<CalculatorClientRequest, CalculatorRequestParams, Long> {

    CalculatorClientRequestTest() {
        super(CalculatorClientRequest.class, CalculatorRequestParams.class, Long.class);
    }
}
