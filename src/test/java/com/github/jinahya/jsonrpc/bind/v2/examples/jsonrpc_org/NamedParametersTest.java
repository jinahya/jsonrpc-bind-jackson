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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.BeanValidationTests.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.readValueFromResource;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.readValueFromResource2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class NamedParametersTest {

    @Test
    void named_parameters_01_request() throws IOException {
        {
            final JacksonRequest<SubtractParams, Integer> request = readValueFromResource2(
                    "named_parameters_01_request.json",
                    JacksonRequest.javaTypeFor(OBJECT_MAPPER, SubtractParams.class, Integer.class),
                    getClass());
            final SubtractParams params = request.getParams();
            assertNotNull(params);
            assertEquals(23, params.getSubtrahend());
            assertEquals(42, params.getMinuend());
            assertEquals(3, (int) request.getId());
        }
        {
            final JacksonServerRequest request = readValueFromResource(
                    "named_parameters_01_request.json", JacksonServerRequest.class, getClass());
            final SubtractParams params = request.getParamsAsNamed(OBJECT_MAPPER, SubtractParams.class);
            assertNotNull(params);
            assertEquals(23, params.getSubtrahend());
            assertEquals(42, params.getMinuend());
            assertEquals(3, request.getId().asInt());
        }
    }

    @Test
    void named_parameters_02_request() throws IOException {
        {
            final NamedParametersRequest clientRequest
                    = readValueFromResource("named_parameters_02_request.json", NamedParametersRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(42, params.getMinuend());
                assertEquals(23, params.getSubtrahend());
                assertEquals(4, (int) clientRequest.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientRequest);
            final NamedParametersServerRequest serverRequest
                    = OBJECT_MAPPER.readValue(string, NamedParametersServerRequest.class);
            {
                final JsonNode params = serverRequest.getParams();
                assertNotNull(params);
                assertTrue(params.isObject());
                assertEquals(42, params.get("minuend").asInt());
                assertEquals(23, params.get("subtrahend").asInt());
                final ValueNode id = serverRequest.getId();
                assertNotNull(id);
                assertTrue(id.isIntegralNumber());
                assertEquals(4, id.asInt());
            }
        }
        {
            final NamedParametersServerRequest serverRequest
                    = readValueFromResource("named_parameters_02_request.json", NamedParametersServerRequest.class);
            {
                final JsonNode params = serverRequest.getParams();
                assertNotNull(params);
                assertTrue(params.isObject());
                assertEquals(42, params.get("minuend").asInt());
                assertEquals(23, params.get("subtrahend").asInt());
                final ValueNode id = serverRequest.getId();
                assertNotNull(id);
                assertTrue(id.isIntegralNumber());
                assertEquals(4, id.asInt());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(serverRequest);
            final NamedParametersRequest clientRequest
                    = OBJECT_MAPPER.readValue(string, NamedParametersRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(42, params.getMinuend());
                assertEquals(23, params.getSubtrahend());
                assertEquals(4, (int) clientRequest.getId());
            }
        }
    }

    @Test
    void named_parameters_01_response() throws IOException {
        {
            final NamedParametersResponse clientResponse = requireValid(readValueFromResource(
                    "named_parameters_01_response.json", NamedParametersResponse.class));
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(3, clientResponse.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientResponse);
            final NamedParametersServerResponse serverResponse
                    = requireValid(OBJECT_MAPPER.readValue(string, NamedParametersServerResponse.class));
            {
                assertEquals(19, serverResponse.getResult().asInt());
                assertEquals(3, serverResponse.getId().asInt());
            }
        }
        {
            final NamedParametersServerResponse serverResponse = requireValid(readValueFromResource(
                    "named_parameters_01_response.json", NamedParametersServerResponse.class));
            {
                assertEquals(19, serverResponse.getResult().asInt());
                assertEquals(3, serverResponse.getId().asInt());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(serverResponse);
            final NamedParametersResponse clientResponse
                    = OBJECT_MAPPER.readValue(string, NamedParametersResponse.class);
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(3, clientResponse.getId());
            }
        }
    }

    @Test
    void named_parameters_02_response() throws IOException {
        {
            final NamedParametersResponse clientResponse = requireValid(readValueFromResource(
                    "named_parameters_02_response.json", NamedParametersResponse.class));
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(4, clientResponse.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientResponse);
            final NamedParametersServerResponse serverResponse
                    = requireValid(OBJECT_MAPPER.readValue(string, NamedParametersServerResponse.class));
            {
                assertEquals(19, serverResponse.getResult().asInt());
                assertEquals(4, serverResponse.getId().asInt());
            }
        }
        {
            final NamedParametersServerResponse serverResponse = requireValid(readValueFromResource(
                    "named_parameters_02_response.json", NamedParametersServerResponse.class));
            {
                assertEquals(19, serverResponse.getResult().asInt());
                assertEquals(4, serverResponse.getId().asInt());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(serverResponse);
            final NamedParametersResponse clientResponse
                    = OBJECT_MAPPER.readValue(string, NamedParametersResponse.class);
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(4, clientResponse.getId());
            }
        }
    }
}
