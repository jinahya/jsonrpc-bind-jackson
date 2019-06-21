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

/**
 * An abstract class for server-side request object.
 *
 * @param <IdType> id type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public abstract class JacksonServerRequest<IdType extends ValueNode> extends JacksonRequest<JsonNode, IdType> {

    /**
     * Check whether specified value node is an instance of either {@link TextNode}, {@link NumericNode}, or {@link
     * NullNode}.
     *
     * @return {@code true} if {@code valueNode} is an instance of either {@link TextNode}, {@link NumericNode}, or
     * {@link NullNode}; {@code false} otherwise.
     */
    static boolean isEitherTextNumberOrNull(final ValueNode valueNode) {
        if (valueNode == null) {
            throw new NullPointerException("valueNode is null");
        }
        return valueNode instanceof TextNode || valueNode instanceof NumericNode || valueNode instanceof NullNode;
    }

    /**
     * Checks whether the current value of {@link #PROPERTY_NAME_PARAMS} property is an instance of either {@link
     * ArrayNode}, {@link ObjectNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #PROPERTY_NAME_PARAMS} property is an instance of either {@link ArrayNode}, {@link
     * * ObjectNode}, or {@link NullNode}; {@code false} otherwise.
     */
    @AssertTrue(message = "a non-null params must be either ArrayNode, ObjectNode, or NullNode")
    private boolean isParamsEitherArrayObjectOrNull() {
        final JsonNode params = getParams();
        return params == null
               || params instanceof ArrayNode
               || params instanceof ObjectNode
               || params instanceof NullNode;
    }

    /**
     * Check whether the current value of {@value #PROPERTY_NAME_ID} property is an instance of either {@link TextNode},
     * {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #getId()} is {@code null} or is an instance of either {@link TextNode}, {@link
     * NumericNode}, or {@link NullNode}; {@code false} otherwise.
     */
    @AssertTrue(message = "a non-null id must be either TextNode, NumericNode, or NullNode")
    private boolean isIdEitherTextNumberOrNull() {
        final IdType id = getId();
        return id == null || isEitherTextNumberOrNull(id);
    }

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
        if (params == null) {
            throw new IllegalStateException("params is currently null");
        }
        if (params instanceof NullNode) {
            return null;
        }
        if (!(params instanceof ObjectNode)) {
            throw new IllegalStateException("params(" + params + ") is not an instance of " + ObjectNode.class);
        }
        final String valueString = objectMapper.writeValueAsString(params);
        return objectMapper.readValue(valueString, paramsClass);
    }

    public <T> List<? extends T> getParamsAsPositioned(final ObjectMapper objectMapper, final Class<?> elementClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null) {
            throw new IllegalStateException("params is currently null");
        }
        if (params instanceof NullNode) {
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
}
