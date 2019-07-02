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
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerRequest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.github.jinahya.jsonrpc.bind.BeanValidationTests.isValid;
import static com.github.jinahya.jsonrpc.bind.BeanValidationTests.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.readTreeFromResource;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class BatchTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void batch_01_request() throws IOException {
        final JsonNode array = readTreeFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/batch_01_request.json");
        final List<JacksonServerRequest> requests = StreamSupport.stream(array.spliterator(), false)
                .map(JacksonServerRequest::of)
                .collect(toList());
        assertEquals(6, requests.size());
        {
            final JacksonServerRequest request = requireValid(requests.get(0));
            assertEquals("sum", request.getMethod());
            final List<Integer> params = request.getParamsAsPositional(OBJECT_MAPPER, Integer.class);
            assertIterableEquals(asList(1, 2, 4), params);
            assertEquals("1", request.getId().asText());
        }
        {
            final JacksonServerRequest request = requireValid(requests.get(1));
            assertEquals("notify_hello", request.getMethod());
            final List<Integer> params = request.getParamsAsPositional(OBJECT_MAPPER, Integer.class);
            assertIterableEquals(singletonList(7), params);
            assertNull(request.getId());
        }
        {
            final JacksonServerRequest request = requireValid(requests.get(2));
            assertEquals("subtract", request.getMethod());
            final List<Integer> params = request.getParamsAsPositional(OBJECT_MAPPER, Integer.class);
            assertIterableEquals(asList(42, 23), params);
            assertEquals("2", request.getId().asText());
        }
        {
            final JacksonServerRequest request = requests.get(3);
            assertFalse(isValid(request));
        }
        {
            final JacksonServerRequest request = requireValid(requests.get(4));
            assertEquals("foo.get", request.getMethod());
            final JsonNode params = request.getParams();
            assertEquals("myself", params.get("name").asText());
            assertEquals("5", request.getId().asText());
        }
        {
            final JacksonServerRequest request = requireValid(requests.get(5));
            assertEquals("get_data", request.getMethod());
            assertNull(request.getParams());
            assertEquals("9", request.getId().asText());
        }
    }

    /**
     * Tests with {@code /.../jsonrpc_org/batch_01_response.json}.
     *
     * @throws IOException if an I/o error occurs.
     */
    @Test
    void batch_01_response() throws IOException {
        final JsonNode array = readTreeFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/batch_01_response.json");
        final List<JacksonServerResponse> responses = StreamSupport.stream(array.spliterator(), false)
                .map(JacksonServerResponse::of)
                .collect(toList());
        assertEquals(5, responses.size());
        {
            final JacksonServerResponse response = requireValid(responses.get(0));
            assertEquals(7, response.getResult().asInt());
            assertEquals("1", response.getId().asText());
        }
        {
            final JacksonServerResponse response = requireValid(responses.get(1));
            assertEquals(19, response.getResult().asInt());
            assertEquals("2", response.getId().asText());
        }
        {
            final JacksonServerResponse response = requireValid(responses.get(2));
            assertEquals(ResponseObject.ErrorObject.CODE_INVALID_REQUEST, response.getError().getCode());
            assertEquals("Invalid Request", response.getError().getMessage());
            assertTrue(response.getId() instanceof NullNode);
        }
        {
            final JacksonServerResponse response = requireValid(responses.get(3));
            assertEquals(ResponseObject.ErrorObject.CODE_METHOD_NOT_FOUND, response.getError().getCode());
            assertEquals("Method not found", response.getError().getMessage());
            assertEquals("5", response.getId().asText());
        }
        {
            final JacksonServerResponse response = requireValid(responses.get(4));
            assertEquals("hello", response.getResult().get(0).asText());
            assertEquals(5, response.getResult().get(1).asInt());
            assertEquals("9", response.getId().asText());
        }
    }
}
