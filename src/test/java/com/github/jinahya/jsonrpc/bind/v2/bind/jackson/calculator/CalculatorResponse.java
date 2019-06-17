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

import com.github.jinahya.jsonrpc.v2.bind.jackson.JacksonResponse;

import java.math.BigDecimal;

public class CalculatorResponse extends JacksonResponse<BigDecimal, CalculatorResponseError> {

    /**
     * Creates a new instance whose {@value #PROPERTY_NAME_RESULT} property set with specified value.
     *
     * @param result the value for {@value #PROPERTY_NAME_RESULT} property.
     * @return a new instance with specified {@code result}.
     */
    public static CalculatorResponse of(final BigDecimal result) {
        final CalculatorResponse instance = new CalculatorResponse();
        instance.setResultExclusively(result);
        return instance;
    }

    /**
     * Creates a new instance whose {@value #PROPERTY_NAME_ERROR} property set with specified value.
     *
     * @param error the value for {@value #PROPERTY_NAME_ERROR} property.
     * @return a new instance with specified {@code error}.
     */
    public static CalculatorResponse of(final CalculatorResponseError error) {
        final CalculatorResponse instance = new CalculatorResponse();
        instance.setErrorExclusively(error);
        return instance;
    }
}
