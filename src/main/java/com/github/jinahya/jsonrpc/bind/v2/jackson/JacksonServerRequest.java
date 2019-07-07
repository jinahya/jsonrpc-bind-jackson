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
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.io.IOException;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.javaType;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.readArray;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.readArrayElementAt;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.readObject;
import static java.util.Optional.ofNullable;

/**
 * A class for lazily mappable request objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonServerRequest extends JacksonRequest<JsonNode, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance whose properties are set from specified json node.
     *
     * @param node the json node from which properties are set.
     * @return a new instance.
     */
    public static JacksonServerRequest of(final JsonNode node) {
        final String jsonrpc = ofNullable(node.get(PROPERTY_NAME_JSONRPC)).map(JsonNode::asText).orElse(null);
        final String method = ofNullable(node.get(PROPERTY_NAME_METHOD)).map(JsonNode::asText).orElse(null);
        final JsonNode params = node.get(PROPERTY_NAME_PARAMS);
        final ValueNode id = (ValueNode) node.get(PROPERTY_NAME_ID);
        return of(JacksonServerRequest.class, jsonrpc, method, params, id);
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
     * Maps the current value of {@link #PROPERTY_NAME_PARAMS} property as a named parameters of specified type.
     *
     * @param mapper an object mapper.
     * @param type   the type to map the value.
     * @param <T>    value type parameter.
     * @return an instance of specified params class; {@code null} if {@link #getParams()} method returns {@code null}
     * or an instance of {@link NullNode}.
     * @throws IOException if an I/O error occurs.
     * @see #getParamsAsNamed(ObjectMapper, Class)
     */
    public <T> T getParamsAsNamed(final ObjectMapper mapper, final JavaType type) throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        return readObject(mapper, params, type);
    }

    /**
     * Maps the current value of {@link #PROPERTY_NAME_PARAMS} property as a named parameters of specified class.
     *
     * @param mapper an object mapper.
     * @param clazz  the class to map the value.
     * @param <T>    value type parameter.
     * @return an instance of specified params class; {@code null} if {@link #getParams()} method returns {@code null}
     * or an instance of {@link NullNode}.
     * @throws IOException if an I/O error occurs.
     * @see #getParamsAsNamed(ObjectMapper, JavaType)
     */
    public <T> T getParamsAsNamed(final ObjectMapper mapper, final Class<? extends T> clazz) throws IOException {
        return getParamsAsNamed(mapper, javaType(mapper.getTypeFactory(), clazz));
    }

    /**
     * Maps the current value of {@link #PROPERTY_NAME_PARAMS} property as a positional parameters of specified element
     * type.
     *
     * @param mapper an object mapper.
     * @param type   the element type.
     * @param <U>    element type parameter
     * @return a list of parameters.
     * @throws IOException if an I/O error occurs.
     */
    public <U> List<U> getParamsAsPositional(final ObjectMapper mapper, final JavaType type) throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        return readArray(mapper, params, type);
    }

    /**
     * Maps the current value of {@link #PROPERTY_NAME_PARAMS} property as a positional parameters of specified element
     * class.
     *
     * @param mapper an object mapper.
     * @param clazz  the element class.
     * @param <U>    element type parameter
     * @return a list of parameters.
     * @throws IOException if an I/O error occurs.
     */
    public <U> List<U> getParamsAsPositional(final ObjectMapper mapper, final Class<? extends U> clazz)
            throws IOException {
        return getParamsAsPositional(mapper, javaType(mapper.getTypeFactory(), clazz));
    }

    /**
     * Maps the single element in the current value of {@link #PROPERTY_NAME_PARAMS} property positioned at specified
     * index as specified type.
     *
     * @param mapper   an object mapper.
     * @param position the position of the element.
     * @param type     the type of the element.
     * @param <U>      element type parameter
     * @return the mapped value of the param.
     * @throws IOException if an I/O error occurs.
     * @see #getParamPositionedAt(ObjectMapper, int, Class)
     */
    public <U> U getParamPositionedAt(final ObjectMapper mapper, final int position, final JavaType type)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null || params instanceof NullNode) {
            return null;
        }
        return readArrayElementAt(mapper, params, position, type);
    }

    /**
     * Maps the element in the current value of {@link #PROPERTY_NAME_PARAMS} property positioned at specified index as
     * specified class.
     *
     * @param mapper   an object mapper.
     * @param position the position of the element.
     * @param clazz    the class of the element.
     * @param <U>      element type parameter
     * @return the mapped value of the param.
     * @throws IOException if an I/O error occurs.
     * @see #getParamPositionedAt(ObjectMapper, int, JavaType)
     */
    public <U> U getParamPositionedAt(final ObjectMapper mapper, final int position, final Class<? extends U> clazz)
            throws IOException {
        return getParamPositionedAt(mapper, position, javaType(mapper.getTypeFactory(), clazz));
    }
}
