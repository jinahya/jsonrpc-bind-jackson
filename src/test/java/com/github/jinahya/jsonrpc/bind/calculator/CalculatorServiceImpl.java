package com.github.jinahya.jsonrpc.bind.calculator;

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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@Slf4j
public class CalculatorServiceImpl implements CalculatorService {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public BigDecimal add(final CalculatorRequestParams.AdditionParams additionParams) {
        return calculator.add(additionParams.getAddend(), additionParams.getAugend());
    }

    @Override
    public BigDecimal subtract(final CalculatorRequestParams.SubtractionParams subtractionParams) {
        return calculator.subtract(subtractionParams.getMinuend(), subtractionParams.getSubtrahend());
    }

    @Override
    public BigDecimal multiply(final CalculatorRequestParams.MultiplicationParam multiplicationParam) {
        return calculator.multiply(multiplicationParam.getMultiplier(), multiplicationParam.getMultiplicand());
    }

    @Override
    public BigDecimal divide(final CalculatorRequestParams.DivisionParam divisionParam) {
        return calculator.divide(divisionParam.getDividend(), divisionParam.getDivisor(),
                                 divisionParam.getRoundingMode());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private Calculator calculator;
}
