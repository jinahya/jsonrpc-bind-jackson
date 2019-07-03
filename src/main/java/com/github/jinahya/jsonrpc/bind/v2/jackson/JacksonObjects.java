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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

/**
 * Constants and utilities for Jackson objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class JacksonObjects {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Checks whether specified object is either an instance of {@link TextNode}, {@link NumericNode}, {@link
     * NullNode}.
     *
     * @param object the object to to check
     * @return {@code true} if specified object is either an instance of {@link TextNode}, {@link NumericNode}, {@link
     * NullNode}; {@code false} otherwise.
     */
    static boolean isEitherStringNumberOfNull(final Object object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        return object instanceof TextNode || object instanceof NumericNode || object instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <T> T readObject(final ObjectMapper objectMapper, final ObjectNode objectNode, final JavaType valueType)
            throws IOException {
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (objectNode == null) {
            throw new NullPointerException("objectNode is null");
        }
        if (valueType == null) {
            throw new NullPointerException("valueClass is null");
        }
        if (valueType.isArrayType()) {
            throw new IllegalArgumentException("valueType(" + valueType + ") represents an array type");
        }
        return objectMapper.readValue(objectMapper.treeAsTokens(objectNode), valueType);
    }

    static <T> T readObject(final ObjectMapper objectMapper, final ObjectNode objectNode,
                            final Class<? extends T> valueClass)
            throws IOException {
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (objectNode == null) {
            throw new NullPointerException("objectNode is null");
        }
        if (valueClass == null) {
            throw new NullPointerException("valueClass is null");
        }
        if (valueClass.isArray()) {
            throw new IllegalArgumentException("valueClass(" + valueClass + ") represents an array class");
        }
        if (true) {
            final JavaType valueType = objectMapper.getTypeFactory().constructType(valueClass);
            return readObject(objectMapper, objectNode, valueType);
        }
        return objectMapper.treeToValue(objectNode, valueClass);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <U> List<U> readArray(final ObjectMapper objectMapper, final ArrayNode arrayNode, final JavaType elementType)
            throws IOException {
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (arrayNode == null) {
            throw new NullPointerException("arrayNode is null");
        }
        if (elementType == null) {
            throw new NullPointerException("elementType is null");
        }
        final JsonParser paramsTokens = objectMapper.treeAsTokens(arrayNode);
        final CollectionType collectionType
                = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
        return objectMapper.readValue(paramsTokens, collectionType);
    }

    static <U> List<U> readArray(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                                 final Class<? extends U> elementClass)
            throws IOException {
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (arrayNode == null) {
            throw new NullPointerException("arrayNode is null");
        }
        if (elementClass == null) {
            throw new NullPointerException("elementClass is null");
        }
        if (true) {
            final JavaType elementType = objectMapper.getTypeFactory().constructType(elementClass);
            return readArray(objectMapper, arrayNode, elementType);
        }
        final JsonParser arrayTokens = objectMapper.treeAsTokens(arrayNode);
        final CollectionType collectionType
                = objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass);
        return objectMapper.readValue(arrayTokens, collectionType);
    }

    static <U> U readArrayElementAt(final ObjectMapper objectMapper, final ArrayNode arrayNode, final int arrayIndex,
                                    final JavaType elementType)
            throws IOException {
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (arrayNode == null) {
            throw new NullPointerException("arrayNode is null");
        }
        if (arrayIndex < 0) {
            throw new IllegalArgumentException("arrayIndex(" + arrayIndex + ") < 0");
        }
        if (arrayIndex >= arrayNode.size()) {
            throw new IllegalArgumentException(
                    "arrayIndex(" + arrayIndex + ") >= arrayNode.size(" + arrayNode.size() + ")");
        }
        if (elementType == null) {
            throw new NullPointerException("elementType is null");
        }
        final JsonParser paramTokens = objectMapper.treeAsTokens(arrayNode.get(arrayIndex));
        return objectMapper.readValue(paramTokens, elementType);
    }

    static <U> U readArrayElementAt(final ObjectMapper objectMapper, final ArrayNode arrayNode, final int arrayIndex,
                                    final Class<? extends U> elementClass)
            throws IOException {
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (arrayNode == null) {
            throw new NullPointerException("arrayNode is null");
        }
        if (arrayIndex < 0) {
            throw new IllegalArgumentException("arrayIndex(" + arrayIndex + ") < 0");
        }
        if (arrayIndex >= arrayNode.size()) {
            throw new IllegalArgumentException(
                    "arrayIndex(" + arrayIndex + ") >= arrayNode.size(" + arrayNode.size() + ")");
        }
        if (elementClass == null) {
            throw new NullPointerException("elementClass is null");
        }
        if (true) {
            final JavaType elementType = objectMapper.getTypeFactory().constructType(elementClass);
            return readArrayElementAt(objectMapper, arrayNode, arrayIndex, elementType);
        }
        final JsonParser paramTokens = objectMapper.treeAsTokens(arrayNode.get(arrayIndex));
        return objectMapper.readValue(paramTokens, elementClass);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonObjects() {
        super();
    }
}
