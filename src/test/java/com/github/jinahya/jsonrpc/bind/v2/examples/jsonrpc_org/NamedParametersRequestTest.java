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
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequestTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NamedParametersRequestTest extends JacksonRequestTest<NamedParametersRequest, SubtractParams, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    NamedParametersRequestTest() {
        super(NamedParametersRequest.class, SubtractParams.class, Integer.TYPE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void named_parameters_01_request() throws IOException {
        final NamedParametersRequest request = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_request.json");
        final SubtractParams params = request.getParams();
        assertNotNull(params);
        assertEquals(23, params.getSubtrahend());
        assertEquals(42, params.getMinuend());
        assertEquals(3, request.getId().intValue());
    }

    @Test
    void named_parameters_02_request() throws IOException {
        final NamedParametersRequest request = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_request.json");
        final SubtractParams params = request.getParams();
        assertNotNull(params);
        assertEquals(23, params.getSubtrahend());
        assertEquals(42, params.getMinuend());
        assertEquals(4, request.getId().intValue());
    }
}
