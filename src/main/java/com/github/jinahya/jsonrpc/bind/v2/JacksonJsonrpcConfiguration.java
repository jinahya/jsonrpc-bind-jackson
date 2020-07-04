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

import static java.util.Objects.requireNonNull;

/**
 * A configuration class for JSON-RPC 2.0.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JacksonJsonrpcConfiguration {

    private static ObjectMapper objectMapper;

    /**
     * Returns current object mapper.
     *
     * @return current object mapper.
     */
    public static synchronized ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Replaces current object mapper with specified value.
     *
     * @param objectMapper new object mapper.
     */
    public static synchronized void setObjectMapper(final ObjectMapper objectMapper) {
        JacksonJsonrpcConfiguration.objectMapper = requireNonNull(objectMapper, "objectMapper is null");
    }

    static {
        setObjectMapper(new ObjectMapper());
    }

    private JacksonJsonrpcConfiguration() {
        throw new AssertionError("instantiation is not allowed");
    }
}
