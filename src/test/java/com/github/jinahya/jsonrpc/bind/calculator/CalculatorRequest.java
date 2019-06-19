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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jinahya.jsonrpc.v2.bind.JacksonRequest;

import javax.validation.constraints.NotNull;

public class CalculatorRequest extends JacksonRequest {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String METHOD_ADD = "add";

    public static final String METHOD_SUBTRACT = "subtract";

    public static final String METHOD_MULTIPLY = "multiply";

    public static final String METHOD_DIVIDE = "divide";

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    @Override
    public JsonNode getParams() {
        return super.getParams();
    }
}
