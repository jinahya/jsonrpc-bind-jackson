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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorImpl implements Calculator {

    @Override
    public BigDecimal add(final BigDecimal addend, final BigDecimal augend) {
        return addend.add(augend);
    }

    @Override
    public BigDecimal subtract(final BigDecimal minuend, final BigDecimal subtrahend) {
        return minuend.subtract(subtrahend);
    }

    @Override
    public BigDecimal multiply(final BigDecimal multiplier, final BigDecimal multiplicand) {
        return multiplier.multiply(multiplicand);
    }

    @Override
    public BigDecimal divide(final BigDecimal dividend, final BigDecimal divisor) {
        return dividend.divide(divisor);
    }

    @Override
    public BigDecimal divide(final BigDecimal dividend, final BigDecimal divisor, final RoundingMode roundingMode) {
        return dividend.divide(divisor, roundingMode);
    }
}
