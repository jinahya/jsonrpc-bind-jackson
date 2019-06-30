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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.jinahya.jsonrpc.bind.JacksonTests;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse.JacksonServerError;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidRequestTest {

    @Test
    void invalid_request_object_01_response() throws IOException {
        {
            final JavaType paramsType = OBJECT_MAPPER.getTypeFactory().constructParametricType(
                    JacksonResponse.class, JsonNode.class, JacksonServerError.class, JsonNode.class);
            final JacksonResponse<JsonNode, ErrorObject<JsonNode>, JsonNode> response
                    = JacksonTests.readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/invalid_request_object_01_response.json",
                    paramsType);
            assertEquals(ErrorObject.CODE_INVALID_REQUEST, response.getError().getCode());
            assertEquals("Invalid Request", response.getError().getMessage());
        }
    }
}
