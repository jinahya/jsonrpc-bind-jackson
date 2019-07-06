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
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * Constants and utilities for jackson objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class JacksonObjects {

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
    static <T extends JsonNode> Optional<T> ofNullableNode(final T jsonNode) {
        return ofNullable(jsonNode).filter(v -> v instanceof NullNode);
    }

    static <T extends JsonNode, R> Optional<R> ofNullableNodeMap(
            final T jsonNode, final Function<? super T, R> function) {
        return ofNullableNode(jsonNode).map(function);
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

    public static <T> T readObject(final ObjectMapper mapper, final JsonNode node, final Class<? extends T> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return readObject(mapper, node, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    public static <U> List<U> readArray(final ObjectMapper mapper, final JsonNode node, final Class<? extends U> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return readArray(mapper, node, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <U> U readArrayElementAt(final ObjectMapper mapper, final JsonNode node, final int index,
                                           final JavaType type)
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
        return mapper.readValue(mapper.treeAsTokens(requireArrayNode(node).get(index)), type);
    }

    public static <U> U readArrayElementAt(final ObjectMapper mapper, final JsonNode node, final int index,
                                           final Class<? extends U> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return readArrayElementAt(mapper, node, index, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T> T readId(final ObjectMapper mapper, final JsonNode node, final JavaType type)
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
        return mapper.readValue(mapper.treeAsTokens(requireValueNode(node)), type);
    }

    public static <T> T readId(final ObjectMapper mapper, final JsonNode node, final Class<? extends T> clazz)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("mapper is null");
        }
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        return readId(mapper, node, javaType(mapper.getTypeFactory(), clazz));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonObjects() {
        super();
    }
}
