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
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * A base class for server-side request objects.
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
     * Returns current value of {@link #getParams()} translated to specified type.
     *
     * @param objectMapper an object mapper.
     * @param paramsClass  the class to parse the {@value #PROPERTY_NAME_PARAMS} property.
     * @param <T>          value type parameter.
     * @return the value of {@value #PROPERTY_NAME_PARAMS} property mapped to specified params class.
     * @throws IOException if an I/O error occurs.
     */
    public <T> T getParamsAsNamed(final ObjectMapper objectMapper, final Class<? extends T> paramsClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!(params instanceof ObjectNode)) {
            throw new IllegalStateException("params(" + params + ") is not an instance of " + ObjectNode.class);
        }
        return objectMapper.treeToValue(params, paramsClass);
    }

    /**
     * Returns the value of {@value #PROPERTY_NAME_PARAMS} property as a positioned parameters which each element mapped
     * as specified element class.
     *
     * @param objectMapper an object mapper.
     * @param elementClass the element class.
     * @param <T>          element type parameter
     * @return a list of specified element class; {@code null} if {@link #getParams()} returns {@code null} or an
     * instance of {@link NullNode}.
     * @throws IOException if an I/O error occurs.
     */
    public <T> List<T> getParamsAsPositioned(final ObjectMapper objectMapper, final Class<? extends T> elementClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!(params instanceof ArrayNode)) {
            throw new IllegalStateException("params(" + params + ") is not an instance of " + ArrayNode.class);
        }
        final String valueString = objectMapper.writeValueAsString(params);
        final CollectionType collectionType
                = objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass);
        return objectMapper.readValue(valueString, collectionType);
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
