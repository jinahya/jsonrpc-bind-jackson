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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.getObjectMapper;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.get;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.set;
import static java.util.Objects.requireNonNull;

final class IJacksonJsonrpcObjectHelper {

    // -----------------------------------------------------------------------------------------------------------------
    static <T> List<T> arrayToList(final ArrayNode node, final Class<T> clazz) {
        requireNonNull(node, "node is null");
        requireNonNull(clazz, "clazz is null");
        final ObjectMapper mapper = getObjectMapper();
        final CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return mapper.convertValue(node, type);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    static ArrayNode listToArray(final List<?> list) {
        requireNonNull(list, "list is null");
        try {
            return getObjectMapper().valueToTree(list);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    // ------------------------------------------------------------------------------------------ unrecognizedProperties
    static final String PROPERTY_NAME_UNRECOGNIZED_PROPERTIES = "unrecognizedProperties";

    @SuppressWarnings({"unchecked"})
    static Map<String, Object> getUnrecognizedProperties(final Class<?> clazz, final Object object) {
        return (Map<String, Object>) get(clazz, PROPERTY_NAME_UNRECOGNIZED_PROPERTIES, object);
    }

    static void setUnrecognizedProperties(final Class<?> clazz, final Object object, final Map<String, Object> value) {
        set(clazz, PROPERTY_NAME_UNRECOGNIZED_PROPERTIES, object, value);
    }

    static Map<String, Object> unrecognizedProperties(final Class<?> clazz, final Object object) {
        final Map<String, Object> unrecognizedProperties = getUnrecognizedProperties(clazz, object);
        if (unrecognizedProperties == null) {
            setUnrecognizedProperties(clazz, object, new HashMap<>());
            return unrecognizedProperties(clazz, object);
        }
        return unrecognizedProperties;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private IJacksonJsonrpcObjectHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
