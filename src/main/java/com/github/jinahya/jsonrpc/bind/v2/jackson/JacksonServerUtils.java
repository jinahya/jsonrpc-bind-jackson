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

/**
 * Constants and utilities for Jackson.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JacksonServerUtils {

//    // -----------------------------------------------------------------------------------------------------------------
//    private static Map<Class<?>, JavaType> JAVA_TYPES;
//
//    private static Map<Class<?>, JavaType> javaTypes() {
//        if (JAVA_TYPES == null) {
//            JAVA_TYPES = new WeakHashMap<>();
//        }
//        return JAVA_TYPES;
//    }
//
//    static JavaType javaType(final TypeFactory factory, final Class<?> clazz) {
//        if (factory == null) {
//            throw new NullPointerException("factory is null");
//        }
//        if (clazz == null) {
//            throw new NullPointerException("clazz is null");
//        }
//        return javaTypes().computeIfAbsent(clazz, factory::constructType);
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    private static Map<JavaType, CollectionType> COLLECTION_TYPES;
//
//    private static Map<JavaType, CollectionType> collectionTypes() {
//        if (COLLECTION_TYPES == null) {
//            COLLECTION_TYPES = new WeakHashMap<>();
//        }
//        return COLLECTION_TYPES;
//    }
//
//    static CollectionType collectionType(final TypeFactory factory, final JavaType type) {
//        if (factory == null) {
//            throw new NullPointerException("factory is null");
//        }
//        if (type == null) {
//            throw new NullPointerException("type is null");
//        }
//        return collectionTypes().computeIfAbsent(type, t -> factory.constructCollectionType(List.class, t));
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    public static <T> T readObject(final ObjectMapper mapper, final JsonNode node, final JavaType type)
//            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (node == null) {
//            throw new NullPointerException("node is null");
//        }
//        if (!node.isObject()) {
//            throw new IllegalArgumentException("node(" + node + ") is not an object node");
//        }
//        if (type == null) {
//            throw new NullPointerException("type is null");
//        }
//        if (false && type.isArrayType()) {
//            throw new IllegalArgumentException("type(" + type + ") is an array type");
//        }
//        return mapper.readValue(mapper.treeAsTokens(node), type);
//    }
//
//    public static <T> T readObject(final ObjectMapper mapper, final JsonNode node, final Class<? extends T> clazz)
//            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (clazz == null) {
//            throw new NullPointerException("clazz is null");
//        }
//        if (false && clazz.isArray()) {
//            throw new IllegalArgumentException("clazz(" + clazz + ") represents an array class");
//        }
//        return readObject(mapper, node, javaType(mapper.getTypeFactory(), clazz));
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    public static <U> List<U> readArray(final ObjectMapper mapper, final JsonNode node, final JavaType type)
//            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (node == null) {
//            throw new NullPointerException("node is null");
//        }
//        if (!node.isArray()) {
//            throw new IllegalArgumentException("node(" + node + ") is not an array node");
//        }
//        if (type == null) {
//            throw new NullPointerException("type is null");
//        }
//        return mapper.readValue(mapper.treeAsTokens(node), collectionType(mapper.getTypeFactory(), type));
//    }
//
//    public static <U> List<U> readArray(final ObjectMapper mapper, final JsonNode node, final Class<? extends U> clazz)
//            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (clazz == null) {
//            throw new NullPointerException("clazz is null");
//        }
//        return readArray(mapper, node, javaType(mapper.getTypeFactory(), clazz));
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    public static <U> U readArrayElementAt(final ObjectMapper mapper, final JsonNode node, final int index,
//                                           final JavaType type)
//            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (node == null) {
//            throw new NullPointerException("node is null");
//        }
//        if (!node.isArray()) {
//            throw new IllegalArgumentException("node(" + node + ") is not an array node");
//        }
//        if (index < 0) {
//            throw new IllegalArgumentException("index(" + index + ") < 0");
//        }
//        if (index >= node.size()) {
//            throw new IllegalArgumentException("index(" + index + ") >= node.size(" + node.size() + ")");
//        }
//        if (type == null) {
//            throw new NullPointerException("elementType is null");
//        }
//        return mapper.readValue(mapper.treeAsTokens(node.get(index)), type);
//    }
//
//    public static <U> U readArrayElementAt(final ObjectMapper mapper, final JsonNode node, final int index,
//                                           final Class<? extends U> clazz)
//            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (clazz == null) {
//            throw new NullPointerException("clazz is null");
//        }
//        return readArrayElementAt(mapper, node, index, javaType(mapper.getTypeFactory(), clazz));
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    public static <T> T readId(final ObjectMapper mapper, final JsonNode node, final JavaType type)
//            throws IOException {
//        return mapper.readValue(mapper.treeAsTokens(requireValueNode(requireNonNull(node))), type);
//    }
//
//    public static <T> T readId(final ObjectMapper mapper, final JsonNode node, final Class<? extends T> clazz)
//            throws IOException {
//        return readId(mapper, node, javaType(mapper.getTypeFactory(), clazz));
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonServerUtils() {
        super();
    }
}
