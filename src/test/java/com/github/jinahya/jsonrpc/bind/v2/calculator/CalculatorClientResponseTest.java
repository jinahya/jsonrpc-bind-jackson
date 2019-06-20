package com.github.jinahya.jsonrpc.bind.v2.calculator;

import com.github.jinahya.jsonrpc.bind.v2.JacksonClientResponseTest;

import java.math.BigDecimal;

class CalculatorClientResponseTest
        extends JacksonClientResponseTest<CalculatorClientResponse, BigDecimal, CalculatorResponseError, Long> {

    CalculatorClientResponseTest() {
        super(CalculatorClientResponse.class, BigDecimal.class, CalculatorResponseError.class, Long.class);
    }
}
