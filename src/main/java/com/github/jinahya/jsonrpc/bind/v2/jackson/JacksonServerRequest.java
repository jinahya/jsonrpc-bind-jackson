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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

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

    // ---------------------------------------------------------------------------------------------------------- params

    /**
     * Maps the current value of {@value #PROPERTY_NAME_PARAMS} property as a named parameters of specified params
     * class.
     *
     * @param mapper an object mapper.
     * @param clazz  the class to parse the {@value #PROPERTY_NAME_PARAMS} property.
     * @param <T>    value type parameter.
     * @return an instance of specified params class; {@code null} if {@link #getParams()} method returns {@code null}
     * or an instance of {@link NullNode}.
     * @throws IllegalArgumentException if {@code paramsClass.isArray()} returns {@code true}.
     * @throws IllegalStateException    if {@code getParams().isObject()} returns {@code false}.
     * @throws IOException              if an I/O error occurs.
     * @see Class#isArray()
     * @see JsonNode#isObject()
     * @see ObjectMapper#treeToValue(TreeNode, Class)
     */
    public <T> T getParamsAsNamed(final ObjectMapper mapper, final Class<? extends T> clazz) throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (clazz == null) {
//            throw new NullPointerException("clazz is null");
//        }
        if (clazz.isArray()) {
            throw new IllegalArgumentException("clazz(" + clazz + ") represents an array class");
        }
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isObject()) {
            throw new IllegalStateException("params(" + params + ") is not an object node");
        }
        return mapper.treeToValue(params, clazz);
    }

    public <U> List<U> getParamsAsPositional(final ObjectMapper mapper, final Class<? extends U> clazz)
            throws IOException {
//        if (mapper == null) {
//            throw new NullPointerException("mapper is null");
//        }
//        if (clazz == null) {
//            throw new NullPointerException("clazz is null");
//        }
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        if (!params.isArray()) {
            throw new IllegalStateException("params(" + params + ") is not an array node");
        }
        final JsonParser tokens = mapper.treeAsTokens(params);
        final CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.readValue(tokens, type);
    }

//    @Deprecated
//    public Object getParams(final ObjectMapper objectMapper, final Class<?> namedObjectClass,
//                            final Class<?> positionedElementClass)
//            throws IOException {
//        final JsonNode params = getParams();
//        if (params == null) {
//            throw new IllegalStateException("params property is currently null");
//        }
//        if (params instanceof NullNode) {
//            return null;
//        }
//        if (params instanceof ObjectNode) {
//            return getParamsAsNamed(objectMapper, namedObjectClass);
//        }
//        if (params instanceof ArrayNode) {
//            return getParamsAsPositional(objectMapper, positionedElementClass);
//        }
//        throw new IllegalStateException("unknown param type: " + params);
//    }
//
//    public void setParamsAsPositioned(final ObjectMapper objectMapper, final List<?> paramsValue) {
//        if (paramsValue == null) {
//            setParams(NullNode.getInstance());
//            return;
//        }
//        final ArrayNode params = objectMapper.createArrayNode();
//        for (final Object paramsElement : paramsValue) {
//            params.add(objectMapper.valueToTree(paramsElement));
//        }
//        setParams(params);
//    }
//
//    public void setParamsAsNamed(final ObjectMapper objectMapper, final Map<String, ?> paramsValue) {
//        if (paramsValue == null) {
//            setParams(NullNode.getInstance());
//            return;
//        }
//        final ObjectNode params = objectMapper.createObjectNode();
//        for (final Map.Entry<String, ?> e : paramsValue.entrySet()) {
//            params.set(e.getKey(), objectMapper.valueToTree(e.getValue()));
//        }
//        setParams(params);
//    }
//
//    public void setParamsAsNamed(final ObjectMapper objectMapper, final Object paramsValue) {
//        if (paramsValue == null) {
//            setParams(NullNode.getInstance());
//            return;
//        }
//        setParams(objectMapper.valueToTree(paramsValue));
//    }

//    @Deprecated
//    public void setParams(final ObjectMapper objectMapper, final Object paramsValue) {
//        if (paramsValue == null) {
//            setParams(NullNode.getInstance());
//            return;
//        }
//        if (paramsValue.getClass().isArray()) {
//            setParamsAsPositioned(objectMapper, asList((Object[]) paramsValue));
//            return;
//        }
//    }
}
