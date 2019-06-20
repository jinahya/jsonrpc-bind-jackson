package com.github.jinahya.jsonrpc.bind.v2.calculator;

import com.github.jinahya.jsonrpc.bind.v2.JacksonClientRequest;

import javax.validation.constraints.NotNull;

public class CalculatorClientRequest extends JacksonClientRequest<CalculatorRequestParams, Long> {

    public static final String METHOD_ADD = "add";

    public static final String METHOD_SUBTRACT = "subtract";

    public static final String METHOD_MULTIPLY = "multiply";

    public static final String METHOD_DIVIDE = "divide";

    @NotNull
    @Override
    public CalculatorRequestParams getParams() {
        return super.getParams();
    }
}
