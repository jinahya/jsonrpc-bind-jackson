package com.github.jinahya.jsonrpc.bind.v2.calculator;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CalculatorClientRequestPositioned extends CalculatorClientRequest<List<BigDecimal>> {

    @NotNull
    @Override
    public List<BigDecimal> getParams() {
        return super.getParams();
    }
}
