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

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.IOException;
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * A class for lazily mappable request objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonServerRequest extends JacksonRequest<JsonNode, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------
    public static <T extends JacksonServerRequest> T of(final Class<? extends T> clazz, final ObjectNode node) {
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        final String jsonrpc = ofNullable(node.get(PROPERTY_NAME_JSONRPC)).map(JsonNode::asText).orElse(null);
        final String method = ofNullable(node.get(PROPERTY_NAME_METHOD)).map(JsonNode::asText).orElse(null);
        final JsonNode params = node.get(PROPERTY_NAME_PARAMS);
        final ValueNode id = (ValueNode) node.get(PROPERTY_NAME_ID);
        try {
            return clazz.cast(ofHandle().invokeWithArguments(clazz, jsonrpc, method, params, id));
        } catch (final Throwable thrown) {
            throw new RuntimeException(thrown);
        }
    }

    /**
     * Creates a new instance from specified object node.
     *
     * @param node the object node from which a new instance is created.
     * @return a new instance.
     */
    public static JacksonServerRequest of(final ObjectNode node) {
        return of(JacksonServerRequest.class, node);
    }

    /**
     * Creates a new instance from specified json node. An {@code IllegalArgumentException} will be thrown if given json
     * node is not an object node.
     *
     * @param node the json node from which a new instance is created.
     * @return a new instance.
     * @see JsonNode#getNodeType()
     * @see JsonNodeType#OBJECT
     * @see #of(ObjectNode)
     */
    public static JacksonServerRequest of(final JsonNode node) {
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        final JsonNodeType type = node.getNodeType();
        if (type != JsonNodeType.OBJECT) {
            throw new IllegalArgumentException("node(" + node + ").type(" + type + ") != " + JsonNodeType.OBJECT);
        }
        assert node instanceof ObjectNode;
        return of((ObjectNode) node);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public JacksonServerRequest() {
        super();
    }

    // ---------------------------------------------------------------------------------------------------------- params

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a named parameters of specified params
     * class.
     *
     * @param objectMapper an object mapper.
     * @param paramsClass  the class to map the {@value #PROPERTY_NAME_PARAMS} property.
     * @param <T>          value type parameter.
     * @return an instance of specified params class; {@code null} if {@link #getParams()} method returns {@code null}
     * or an instance of {@link NullNode}.
     * @throws IllegalArgumentException if {@code paramsClass.isArray()} returns {@code true}.
     * @throws IllegalStateException    if {@code getParams().isObject()} returns {@code false}.
     * @throws IOException              if an I/O error occurs.
     * @see Class#isArray()
     * @see JsonNode#isObject()
     * @see ObjectMapper#treeToValue(TreeNode, Class)
     */
    public <T> T getParamsAsNamed(final ObjectMapper objectMapper, final Class<? extends T> paramsClass)
            throws IOException {
        if (paramsClass == null) {
            throw new NullPointerException("paramsClass is null");
        }
        if (paramsClass.isArray()) {
            throw new IllegalArgumentException("paramsClass(" + paramsClass + ") represents an array class");
        }
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isObject()) {
            throw new IllegalStateException("params(" + params + ") is not an object node");
        }
        return JacksonObjects.readObject(objectMapper, (ObjectNode) params, paramsClass);
//        return objectMapper.treeToValue(params, paramsClass);
    }

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a positional parameters of specified element
     * type.
     *
     * @param objectMapper an object mapper.
     * @param paramType    the element type.
     * @param <U>          element type parameter
     * @return a list of parameters.
     * @throws IOException if an I/O error occurs.
     */
    public <U> List<U> getParamsAsPositional(final ObjectMapper objectMapper, final JavaType paramType)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isArray()) {
            throw new IllegalStateException("params(" + params + ") is not an array node");
        }
//        final JsonParser paramsTokens = objectMapper.treeAsTokens(params);
//        final CollectionType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, paramType);
//        return objectMapper.readValue(paramsTokens, valueType);
        return JacksonObjects.readArray(objectMapper, (ArrayNode) params, paramType);
    }

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a positional parameters of specified element
     * class.
     *
     * @param objectMapper an object mapper.
     * @param paramClass   the element class.
     * @param <U>          element type parameter
     * @return a list of parameters.
     * @throws IOException if an I/O error occurs.
     */
    public <U> List<U> getParamsAsPositional(final ObjectMapper objectMapper, final Class<? extends U> paramClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isArray()) {
            throw new IllegalStateException("params(" + params + ") is not an array node");
        }
//        final JsonParser paramsTokens = objectMapper.treeAsTokens(params);
//        final CollectionType collectionType
//                = objectMapper.getTypeFactory().constructCollectionType(List.class, paramClass);
//        return objectMapper.readValue(paramsTokens, collectionType);
        return JacksonObjects.readArray(objectMapper, (ArrayNode) params, paramClass);
    }

    public <U> U getParamAt(final ObjectMapper objectMapper, final int paramIndex, final JavaType paramType)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isArray()) {
            throw new IllegalStateException("params(" + params + ") is not an array node");
        }
//        final JsonParser paramTokens = objectMapper.treeAsTokens(params.get(paramIndex));
//        return objectMapper.readValue(paramTokens, paramType);
        return JacksonObjects.readArrayElementAt(objectMapper, (ArrayNode) params, paramIndex, paramType);
    }

    public <U> U getParamAt(final ObjectMapper objectMapper, final int paramIndex, final Class<? extends U> paramClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isArray()) {
            throw new IllegalStateException("params(" + params + ") is not an array node");
        }
//        final JsonParser paramTokens = objectMapper.treeAsTokens(params.get(paramIndex));
//        return objectMapper.readValue(paramTokens, paramClass);
        return JacksonObjects.readArrayElementAt(objectMapper, (ArrayNode) params, paramIndex, paramClass);
    }
}
