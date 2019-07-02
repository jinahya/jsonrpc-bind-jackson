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

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerRequest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.readValueFromResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class NamedParametersTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void named_parameters_01_request() throws IOException {
        {
            final TypeReference<JacksonRequest<SubtractParams, Integer>> typeReference
                    = new TypeReference<JacksonRequest<SubtractParams, Integer>>() {
            };
            final JacksonRequest<SubtractParams, Integer> request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_request.json",
                    typeReference);
            final SubtractParams params = request.getParams();
            assertNotNull(params);
            assertEquals(23, params.getSubtrahend());
            assertEquals(42, params.getMinuend());
            assertEquals(3, (int) request.getId());
        }
        {
            final JacksonServerRequest request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_request.json",
                    JacksonServerRequest.class);
            final SubtractParams params = request.getParamsAsNamed(OBJECT_MAPPER, SubtractParams.class);
            assertNotNull(params);
            assertEquals(23, params.getSubtrahend());
            assertEquals(42, params.getMinuend());
            assertEquals(3, request.getId().asInt());
            assertThrows(IllegalStateException.class,
                         () -> request.getParamsAsPositional(OBJECT_MAPPER, Integer.class));
        }
    }

    @Test
    void named_parameters_01_response() throws IOException {
        {
            final TypeReference<JacksonResponse<Integer, ErrorObject<?>, Integer>> typeReference
                    = new TypeReference<JacksonResponse<Integer, ErrorObject<?>, Integer>>() {
            };
            final JacksonResponse<Integer, ResponseObject.ErrorObject<?>, Integer> response = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_response.json",
                    typeReference);
            assertEquals(19, response.getResult().intValue());
            assertEquals(3, response.getId().intValue());
        }
        {
            final JacksonServerResponse response = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_response.json",
                    JacksonServerResponse.class);
            assertEquals(19, response.getResult().asInt());
            assertEquals(3, response.getId().asInt());
        }
    }

    @Test
    void named_parameters_02_request() throws IOException {
        {
            final TypeReference<JacksonRequest<SubtractParams, Integer>> typeReference
                    = new TypeReference<JacksonRequest<SubtractParams, Integer>>() {
            };
            final JacksonRequest<SubtractParams, Integer> request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_request.json",
                    typeReference);
            final SubtractParams params = request.getParams();
            assertNotNull(params);
            assertEquals(42, params.getMinuend());
            assertEquals(23, params.getSubtrahend());
            assertEquals(4, (int) request.getId());
        }
        {
            final JacksonServerRequest request = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_request.json",
                    JacksonServerRequest.class);
            final SubtractParams params = request.getParamsAsNamed(OBJECT_MAPPER, SubtractParams.class);
            assertNotNull(params);
            assertEquals(42, params.getMinuend());
            assertEquals(23, params.getSubtrahend());
            assertEquals(4, request.getId().asInt());
            assertThrows(IllegalStateException.class,
                         () -> request.getParamsAsPositional(OBJECT_MAPPER, Integer.class));
        }
    }

    @Test
    void named_parameters_02_response() throws IOException {
        {
            final TypeReference<JacksonResponse<Integer, ErrorObject<?>, Integer>> typeReference
                    = new TypeReference<JacksonResponse<Integer, ErrorObject<?>, Integer>>() {
            };
            final JacksonResponse<Integer, ResponseObject.ErrorObject<?>, Integer> response = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_response.json",
                    typeReference);
            assertEquals(19, response.getResult().intValue());
            assertEquals(4, response.getId().intValue());
        }
        {
            final JacksonServerResponse response = readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_response.json",
                    JacksonServerResponse.class);
            assertEquals(19, response.getResult().asInt());
            assertEquals(4, response.getId().asInt());
        }
    }
}
