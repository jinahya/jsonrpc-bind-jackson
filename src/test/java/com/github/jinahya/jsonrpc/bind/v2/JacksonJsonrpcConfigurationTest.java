package com.github.jinahya.jsonrpc.bind.v2;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.getObjectMapper;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.setObjectMapper;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JacksonJsonrpcConfigurationTest {

    @DisplayName("getObjectMapper() returns non-null")
    @Test
    void assertGetObjectMapperReturnsNonNull() {
        assertNotNull(getObjectMapper());
    }

    @DisplayName("setObjectMapper(null) throws NullPointerException")
    @Test
    void assertSetObjectMapperThrowsNullPointerExceptionWhenObjectMapperIsNull() {
        assertThrows(NullPointerException.class, () -> setObjectMapper(null));
    }

    @Test
    void testSetObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        setObjectMapper(objectMapper);
        assertSame(objectMapper, getObjectMapper());
    }
}
