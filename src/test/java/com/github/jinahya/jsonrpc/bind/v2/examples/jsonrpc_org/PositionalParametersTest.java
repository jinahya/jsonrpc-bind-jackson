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

import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.readValueFromResource;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequest.javaTypeForPositional;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PositionalParametersTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void positional_parameters_01_request() throws IOException {
        {
            final JacksonRequest<List<Integer>, Integer> request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_01_request.json",
                    javaTypeForPositional(OBJECT_MAPPER.getTypeFactory(), List.class, Integer.class, Integer.class));
            assertIterableEquals(asList(42, 23), request.getParams());
            assertEquals(1, request.getId().intValue());
        }
        {
            final JacksonServerRequest request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_01_request.json",
                    JacksonServerRequest.class);
            assertIterableEquals(asList(42, 23), request.getParamsAsPositional(OBJECT_MAPPER, Integer.class));
            assertEquals(1, request.getId().asInt());
        }
    }

    @Test
    void positional_parameters_02_request() throws IOException {
        {
            final JacksonRequest<List<Integer>, Integer> request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_02_request.json",
                    javaTypeForPositional(OBJECT_MAPPER.getTypeFactory(), List.class, Integer.class, Integer.class));
            assertIterableEquals(asList(23, 42), request.getParams());
            assertEquals(2, request.getId().intValue());
        }
        {
            final JacksonServerRequest request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_02_request.json",
                    JacksonServerRequest.class);
            assertIterableEquals(asList(23, 42), request.getParamsAsPositional(OBJECT_MAPPER, Integer.class));
            assertEquals(2, request.getId().asInt());
        }
    }
}
