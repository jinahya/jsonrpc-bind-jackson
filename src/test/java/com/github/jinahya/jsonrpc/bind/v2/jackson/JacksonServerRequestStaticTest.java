package com.github.jinahya.jsonrpc.bind.v2.jackson;

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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JacksonServerRequestStaticTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static class ExtendedJacksonServerRequest extends JacksonServerRequest {

    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testOfWithNonObjectNode() {
        {
            final ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
            assertDoesNotThrow(() -> JacksonServerRequest.of(objectNode));
        }
        final ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
//        assertThrows(IllegalArgumentException.class, () -> JacksonServerRequest.of(arrayNode));
    }
}
