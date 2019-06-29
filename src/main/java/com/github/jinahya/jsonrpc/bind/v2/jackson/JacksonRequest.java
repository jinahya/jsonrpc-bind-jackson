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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import javax.validation.constraints.AssertTrue;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_JSONRPC;
import static com.github.jinahya.jsonrpc.bind.v2.RequestObject.PROPERTY_NAME_METHOD;
import static com.github.jinahya.jsonrpc.bind.v2.RequestObject.PROPERTY_NAME_PARAMS;

/**
 * An base class for request objects.
 *
 * @param <ParamsType> params type parameter
 * @param <IdType>     id type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonPropertyOrder({PROPERTY_NAME_JSONRPC, PROPERTY_NAME_METHOD, PROPERTY_NAME_PARAMS, PROPERTY_NAME_ID})
@JsonInclude(JsonInclude.Include.NON_NULL) // for notification
public class JacksonRequest<ParamsType, IdType> extends RequestObject<ParamsType, IdType> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns an instance of {@link JavaType} for specified parameter classes.
     *
     * @param typeFactory  a type factory.
     * @param paramsClass  the params class.
     * @param idClass      the id class.
     * @param <ParamsType> params type paramter.
     * @param <IdType>     id type parameter.
     * @return a java type for specified parameter classes.
     * @see TypeFactory#constructParametricType(Class, Class[])
     * @see #javaTypeFor(ObjectMapper, Class, Class)
     */
    public static <ParamsType, IdType> JavaType javaTypeFor(final TypeFactory typeFactory,
                                                            final Class<? extends ParamsType> paramsClass,
                                                            final Class<? extends IdType> idClass) {
        return typeFactory.constructParametricType(JacksonRequest.class, paramsClass, idClass);
    }

    /**
     * Returns an instance of {@link JavaType} for specified parameter classes.
     *
     * @param objectMapper a object mapper whose {@link TypeFactory} is used.
     * @param paramsClass  the params class.
     * @param idClass      the id class.
     * @param <ParamsType> params type paramter.
     * @param <IdType>     id type parameter.
     * @return a java type for specified parameter classes.
     * @see #javaTypeFor(TypeFactory, Class, Class)
     */
    public static <ParamsType, IdType> JavaType javaTypeFor(final ObjectMapper objectMapper,
                                                            final Class<? extends ParamsType> paramsClass,
                                                            final Class<? extends IdType> idClass) {
        return javaTypeFor(objectMapper.getTypeFactory(), paramsClass, idClass);
    }

    // ---------------------------------------------------------------------------------------------------------- params

    @Override
    protected @AssertTrue boolean isParamsStructured() {
        if (super.isParamsStructured()) {
            return true;
        }
        final ParamsType params = getParams();
        assert params != null;
        return params instanceof ArrayNode || params instanceof ObjectNode || params instanceof NullNode;
    }

    // -------------------------------------------------------------------------------------------------------------- id
    @Override
    protected @AssertTrue boolean isIdEitherStringNumberOfNull() {
        if (super.isIdEitherStringNumberOfNull()) {
            return true;
        }
        final IdType id = getId();
        assert id != null;
        return id instanceof TextNode || id instanceof NumericNode || id instanceof NullNode;
    }
}
