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

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static java.util.Objects.requireNonNull;

/**
 * Constants and utilities for jackson objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JacksonObjects {

    // -----------------------------------------------------------------------------------------------------------------
    private static Map<Class<?>, JavaType> JAVA_TYPES;

    private static Map<Class<?>, JavaType> javaTypes() {
        if (JAVA_TYPES == null) {
            JAVA_TYPES = new WeakHashMap<>();
        }
        return JAVA_TYPES;
    }

    static JavaType javaType(final TypeFactory typeFactory, final Class<?> valueClass) {
        if (typeFactory == null) {
            throw new NullPointerException("typeFactory is null");
        }
        if (valueClass == null) {
            throw new NullPointerException("valueClazz is null");
        }
        return javaTypes().computeIfAbsent(valueClass, typeFactory::constructType);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Map<JavaType, CollectionType> COLLECTION_TYPES;

    private static Map<JavaType, CollectionType> collectionTypes() {
        if (COLLECTION_TYPES == null) {
            COLLECTION_TYPES = new WeakHashMap<>();
        }
        return COLLECTION_TYPES;
    }

    static CollectionType collectionType(final TypeFactory typeFactory, final JavaType elementType) {
        if (typeFactory == null) {
            throw new NullPointerException("typeFactory is null");
        }
        if (elementType == null) {
            throw new NullPointerException("elementType is null");
        }
        return collectionTypes().computeIfAbsent(elementType, t -> typeFactory.constructCollectionType(List.class, t));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Checks whether specified object is either an instance of {@link TextNode}, {@link NumericNode}, {@link
     * NullNode}.
     *
     * @param object the object to check.
     * @return {@code true} if specified object is either an instance of {@link TextNode}, {@link NumericNode}, {@link
     * NullNode}; {@code false} otherwise.
     * @throws NullPointerException if {@code object} is {@code null}.
     */
    static boolean isEitherStringNumberOfNull(final Object object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        return object instanceof TextNode || object instanceof NumericNode || object instanceof NullNode;
    }

    static Object requireEitherStringNumberOfNull(final Object object) {
        if (!isEitherStringNumberOfNull(object)) {
            throw new IllegalArgumentException(
                    "object(" + object + ") is neither " + TextNode.class + ", " + NumericNode.class + " nor "
                    + NullNode.class);
        }
        return object;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static JsonNode requireObjectNode(final JsonNode node) {
        final JsonNodeType type = requireNonNull(node, "node is null").getNodeType();
        if (type != JsonNodeType.OBJECT) {
            throw new IllegalArgumentException("node(" + node + ").type(" + type + ") != " + JsonNodeType.OBJECT);
        }
        return node;
    }

    static JsonNode requireArrayNode(final JsonNode node) {
        final JsonNodeType type = requireNonNull(node, "node is null").getNodeType();
        if (type != JsonNodeType.ARRAY) {
            throw new IllegalArgumentException("node(" + node + ").type(" + type + ") != " + JsonNodeType.ARRAY);
        }
        return node;
    }

    static JsonNode requireValueNode(final JsonNode node) {
        if (!(node instanceof ValueNode)) {
            throw new IllegalArgumentException("node(" + node + ") is not an instance of " + ValueNode.class);
        }
        return node;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads an object value of specified type from specified node.
     *
     * @param mapper an object mapper.
     * @param node   the node form the object is read; must be an instance of {@link com.fasterxml.jackson.databind.node.ObjectNode}.
     * @param type   the type of object.
     * @param <T>    value type parameter
     * @return the value.
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T readObject(final ObjectMapper mapper, final JsonNode node, final JavaType type)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        return mapper.readValue(mapper.treeAsTokens(requireObjectNode(node)), type);
    }

    /**
     * Reads an object value of specified class from specified node.
     *
     * @param mapper an object mapper.
     * @param node   the json node form which the value is read; must be an instance of {@link
     *               com.fasterxml.jackson.databind.node.ObjectNode}.
     * @param clazz  the class of the value.
     * @param <T>    value type parameter
     * @return the value.
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T readObject(final ObjectMapper mapper, final JsonNode node, final Class<? extends T> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        return readObject(mapper, node, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads elements of specified type from specified node.
     *
     * @param mapper an object mapper.
     * @param node   the node to read elements; must be an instance of {@link com.fasterxml.jackson.databind.node.ArrayNode}.
     * @param type   the type of elements.
     * @param <U>    element type parameter
     * @return a list of array elements.
     * @throws IOException if an I/O error occurs.
     */
    public static <U> List<U> readArray(final ObjectMapper mapper, final JsonNode node, final JavaType type)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        return mapper.readValue(mapper.treeAsTokens(requireArrayNode(node)),
                                collectionType(mapper.getTypeFactory(), type));
    }

    /**
     * Reads elements of specified class from specified node.
     *
     * @param mapper an object mapper.
     * @param node   the node to read elements; must be an instance of {@link com.fasterxml.jackson.databind.node.ArrayNode}.
     * @param clazz  the class of elements.
     * @param <U>    element type parameter
     * @return a list of array elements.
     * @throws IOException if an I/O error occurs.
     */
    public static <U> List<U> readArray(final ObjectMapper mapper, final JsonNode node, final Class<? extends U> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        return readArray(mapper, node, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Reads a single element of specified type at specified index from specified node.
     *
     * @param mapper an object mapper.
     * @param node   the node from which the element is read; must be an instance of {@link
     *               com.fasterxml.jackson.databind.node.ArrayNode}.
     * @param index  the index of the element.
     * @param type   the type of the element.
     * @param <U>    element type parameter
     * @return the element at specified index.
     * @throws IOException if an I/O error occurs.
     */
    public static <U> U readArrayElementAt(final ObjectMapper mapper, final JsonNode node, final int index,
                                           final JavaType type)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (index < 0) {
            throw new IllegalArgumentException("index(" + index + ") < 0");
        }
        final int size = requireArrayNode(node).size();
        if (index >= size) {
            throw new IllegalArgumentException("index(" + index + ") >= node.size(" + size + ")");
        }
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        return mapper.readValue(mapper.treeAsTokens(requireArrayNode(node).get(index)), type);
    }

    /**
     * Reads a single element of specified class at specified index from specified node.
     *
     * @param mapper an object mapper.
     * @param node   the node from which the element is read; must be an instance of {@link
     *               com.fasterxml.jackson.databind.node.ArrayNode}.
     * @param index  the index of the element.
     * @param clazz  the class of the element.
     * @param <U>    element type parameter
     * @return the element at specified index.
     * @throws IOException if an I/O error occurs.
     */
    public static <U> U readArrayElementAt(final ObjectMapper mapper, final JsonNode node, final int index,
                                           final Class<? extends U> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        return readArrayElementAt(mapper, node, index, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonObjects() {
        super();
    }
}
