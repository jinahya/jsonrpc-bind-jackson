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
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import javax.validation.constraints.AssertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * A class for lazily mappable request objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonServerRequest extends JacksonRequest<JsonNode, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public JacksonServerRequest() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Indicates whether the current value of {@link #PROPERTY_NAME_PARAMS} property is {@code null} or an instance of
     * either {@link ArrayNode}, {@link ObjectNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #getParams()} returns {@code null} or an instance of either {@link ArrayNode},
     * {@link * ObjectNode}, or {@link NullNode}; {@code false} otherwise.
     * @see #getParams()
     */
    @AssertTrue//(message = "a non-null params must be an instance of either ArrayNode, ObjectNode, or NullNode")
    private boolean isParamsEitherArrayObjectOrNull() {
        final JsonNode params = getParams();
        return params == null
               || params instanceof ArrayNode || params instanceof ObjectNode || params instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Indicates whether the current value of {@value #PROPERTY_NAME_ID} property is {@code null} or an instance of
     * either {@link TextNode}, {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #getId()} returns {@code null} or an instance of either {@link TextNode}, {@link
     * NumericNode}, or {@link NullNode}; {@code false} otherwise.
     * @see #getId()
     */
    @AssertTrue//(message = "a non-null id must be an instance of either TextNode, NumericNode, or NullNode")
    private boolean isIdEitherTextNodeNumericNodeOrNullNode() {
        final ValueNode id = getId();
        return id == null || id instanceof TextNode || id instanceof NumericNode || id instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a named parameters of specified params
     * class.
     *
     * @param objectMapper an object mapper.
     * @param paramsClass  the class to parse the {@value #PROPERTY_NAME_PARAMS} property.
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
        if (objectMapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (paramsClass == null) {
            throw new NullPointerException("paramsClass is null");
        }
        if (paramsClass.isArray()) {
            throw new IllegalArgumentException("paramsClass is array");
        }
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isObject()) {
            throw new IllegalStateException("params(" + params + ") is not an object node");
        }
        return objectMapper.treeToValue(params, paramsClass);
    }

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a positioned parameters which each parameter
     * is an instance of specified element class and add them to specified collection.
     *
     * @param mapper     an object mapper.
     * @param clazz      the element class.
     * @param collection the collection to which positioned parameters are added.
     * @param <T>        collection type parameter
     * @param <U>        element type parameter
     * @return the specified collection
     * @throws IllegalArgumentException if {@code getParams().isArray()} returns {@code false}.
     * @throws IOException              if an I/O error occurs.
     * @see ObjectMapper#treeAsTokens(TreeNode)
     * @see ObjectMapper#readValue(JsonParser, JavaType)
     */
    public <T extends Collection<? super U>, U> T getParamsAsPositioned(
            final ObjectMapper mapper, final Class<? extends U> clazz, final T collection)
            throws IOException {
        if (mapper == null) {
            throw new NullPointerException("objectMapper is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        if (collection == null) {
            throw new NullPointerException("collection is null");
        }
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return collection;
        }
        if (!params.isArray()) {
            throw new IllegalStateException("params(" + params + ") is not an array node");
        }
        final JsonParser tokens = mapper.treeAsTokens(params);
        final CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        collection.addAll(mapper.readValue(tokens, type));
        return collection;
    }

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a positioned parameters which each parameter
     * is an instance of specified element class.
     *
     * @param objectMapper an object mapper.
     * @param elementClass the element class.
     * @param <T>          element type parameter
     * @return a list of specified element class; {@code null} if {@link #getParams()} returns {@code null} or an
     * instance of {@link NullNode}.
     * @throws IllegalArgumentException if {@code getParams().isArray()} returns {@code false}.
     * @throws IOException              if an I/O error occurs.
     * @see ObjectMapper#treeAsTokens(TreeNode)
     * @see ObjectMapper#readValue(JsonParser, JavaType)
     * @see #getParamsAsPositioned(ObjectMapper, Class, Collection)
     */
    public <T> List<T> getParamsAsPositioned(final ObjectMapper objectMapper, final Class<? extends T> elementClass)
            throws IOException {
        return getParamsAsPositioned(objectMapper, elementClass, new ArrayList<>());
    }

    @Deprecated
    public Object getParams(final ObjectMapper objectMapper, final Class<?> namedObjectClass,
                            final Class<?> positionedElementClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null) {
            throw new IllegalStateException("params property is currently null");
        }
        if (params instanceof NullNode) {
            return null;
        }
        if (params instanceof ObjectNode) {
            return getParamsAsNamed(objectMapper, namedObjectClass);
        }
        if (params instanceof ArrayNode) {
            return getParamsAsPositioned(objectMapper, positionedElementClass);
        }
        throw new IllegalStateException("unknown param type: " + params);
    }

    public void setParamsAsPositioned(final ObjectMapper objectMapper, final List<?> paramsValue) {
        if (paramsValue == null) {
            setParams(NullNode.getInstance());
            return;
        }
        final ArrayNode params = objectMapper.createArrayNode();
        for (final Object paramsElement : paramsValue) {
            params.add(objectMapper.valueToTree(paramsElement));
        }
        setParams(params);
    }

    public void setParamsAsNamed(final ObjectMapper objectMapper, final Map<String, ?> paramsValue) {
        if (paramsValue == null) {
            setParams(NullNode.getInstance());
            return;
        }
        final ObjectNode params = objectMapper.createObjectNode();
        for (final Map.Entry<String, ?> e : paramsValue.entrySet()) {
            params.set(e.getKey(), objectMapper.valueToTree(e.getValue()));
        }
        setParams(params);
    }

    public void setParamsAsNamed(final ObjectMapper objectMapper, final Object paramsValue) {
        if (paramsValue == null) {
            setParams(NullNode.getInstance());
            return;
        }
        setParams(objectMapper.valueToTree(paramsValue));
    }

    @Deprecated
    public void setParams(final ObjectMapper objectMapper, final Object paramsValue) {
        if (paramsValue == null) {
            setParams(NullNode.getInstance());
            return;
        }
        if (paramsValue.getClass().isArray()) {
            setParamsAsPositioned(objectMapper, asList((Object[]) paramsValue));
            return;
        }
    }
}
