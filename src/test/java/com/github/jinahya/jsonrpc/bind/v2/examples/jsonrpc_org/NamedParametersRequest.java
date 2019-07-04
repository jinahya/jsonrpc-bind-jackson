package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

/*-
 * #%L
 * jsonrpc-bind-jackson
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

import com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org.NamedParametersRequest.SubtractParams;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequest;
import lombok.Data;

class NamedParametersRequest extends JacksonRequest<SubtractParams, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    @Data
    public static class SubtractParams {

        private int subtrahend;

        private int minuend;
    }
}
