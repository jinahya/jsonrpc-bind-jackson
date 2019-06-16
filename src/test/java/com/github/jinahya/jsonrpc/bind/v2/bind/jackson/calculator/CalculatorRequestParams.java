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

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public interface CalculatorRequestParams {

    class AdditionParams implements CalculatorRequestParams {

        @NotNull
        @Setter
        @Getter
        private BigDecimal augend;

        @NotNull
        @Setter
        @Getter
        private BigDecimal addend;
    }

    class SubtractionParams implements CalculatorRequestParams {

        @NotNull
        @Setter
        @Getter
        private BigDecimal minuend;

        @NotNull
        @Setter
        @Getter
        private BigDecimal subtrahend;
    }

    class MultiplicationParam implements CalculatorRequestParams {

        @NotNull
        @Setter
        @Getter
        private BigDecimal multiplicand;

        @NotNull
        @Setter
        @Getter
        private BigDecimal multiplier;
    }

    class DivisionParam implements CalculatorRequestParams {

        public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

        @NotNull
        @Setter
        @Getter
        private BigDecimal dividend;

        @NotNull
        @Setter
        @Getter
        private BigDecimal divisor;

        @NotNull
        @Setter
        @Getter
        private RoundingMode roundingMode = DEFAULT_ROUNDING_MODE;
    }
}
