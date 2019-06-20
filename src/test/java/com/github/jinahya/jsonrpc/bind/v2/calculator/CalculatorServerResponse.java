package com.github.jinahya.jsonrpc.bind.v2.calculator;

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

import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.JacksonServerResponse;

import java.math.BigDecimal;

public class CalculatorServerResponse extends JacksonServerResponse<BigDecimal, CalculatorResponseError, ValueNode> {

    /**
     * Creates a new instance whose {@value #PROPERTY_NAME_RESULT} property set with specified value.
     *
     * @param result the value for {@value #PROPERTY_NAME_RESULT} property.
     * @param id     id
     * @return a new instance with specified {@code result}.
     */
    public static CalculatorServerResponse of(final BigDecimal result, final ValueNode id) {
        final CalculatorServerResponse instance = new CalculatorServerResponse();
        instance.setResultExclusively(result);
        instance.setId(id);
        return instance;
    }

    /**
     * Creates a new instance whose {@value #PROPERTY_NAME_ERROR} property set with specified value.
     *
     * @param error the value for {@value #PROPERTY_NAME_ERROR} property.
     * @param id    id
     * @return a new instance with specified {@code error}.
     */
    public static CalculatorServerResponse of(final CalculatorResponseError error, final ValueNode id) {
        final CalculatorServerResponse instance = new CalculatorServerResponse();
        instance.setErrorExclusively(error);
        instance.setId(id);
        return instance;
    }
}
