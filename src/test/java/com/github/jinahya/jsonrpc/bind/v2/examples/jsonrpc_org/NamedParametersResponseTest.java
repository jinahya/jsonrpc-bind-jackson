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

import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonErrorTest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse.JacksonError;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponseTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamedParametersResponseTest
        extends JacksonResponseTest<NamedParametersResponse, Integer, JacksonError<Void>, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    NamedParametersResponseTest() {
        super(NamedParametersResponse.class, Integer.class, JacksonErrorTest.NoData.class, Integer.TYPE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void named_parameters_01_response() throws IOException {
        final NamedParametersResponse response = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_response.json");
        assertEquals(19, response.getResult().intValue());
        assertEquals(3, response.getId().intValue());
    }

    @Test
    void named_parameters_02_response() throws IOException {
        final NamedParametersResponse response = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_response.json");
        assertEquals(19, response.getResult().intValue());
        assertEquals(4, response.getId().intValue());
    }
}
