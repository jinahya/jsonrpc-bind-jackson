package com.github.jinahya.jsonrpc.bind.v2.jackson;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcRequestMessage.readValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JacksonJsonrpcRequestMessageTest {

    @Test
    void testUnrecognizedProperties() throws JsonProcessingException {
        final String string1 = "{"
                               + "\"jsonrpc\": 2.0,"
                               + "\"method\": \"method\","
                               + "\"params\": [1],"
                               + "\"id\": 1,"
                               + "\"unknown\": true"
                               + "}";
        final JacksonJsonrpcRequestMessage message = readValue(new StringReader(string1));
        log.debug("unrecognized properties: {}", message.getUnrecognizedProperties());
        assertEquals(1, message.getUnrecognizedProperties().size());
        assertTrue(message.getUnrecognizedProperties().containsKey("unknown"));
        assertTrue((Boolean) message.getUnrecognizedProperties().get("unknown"));
        message.putUnrecognizedProperty("unknown", Boolean.FALSE);
        message.putUnrecognizedProperty("unknown2", "unknown3");
        final String string2 = getObjectMapper().writeValueAsString(message);
        log.debug("string2: {}", string2);
    }
}
