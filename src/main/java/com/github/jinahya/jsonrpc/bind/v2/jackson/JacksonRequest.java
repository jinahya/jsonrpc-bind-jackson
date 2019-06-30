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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import javax.validation.constraints.AssertTrue;
import java.util.Collection;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_JSONRPC;
import static com.github.jinahya.jsonrpc.bind.v2.RequestObject.PROPERTY_NAME_METHOD;
import static com.github.jinahya.jsonrpc.bind.v2.RequestObject.PROPERTY_NAME_PARAMS;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.isEitherStringNumberOfNull;

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
//    static JavaType javaTypeForNamed(final TypeFactory typeFactory, final JavaType paramsType, final JavaType idType) {
//        return typeFactory.constructParametricType(JacksonRequest.class, paramsType, idType);
//    }

    /**
     * Returns an instance of {@link JavaType} for specified parameter classes.
     *
     * @param typeFactory  a type factory.
     * @param paramsClass  the params class.
     * @param idClass      the id class.
     * @param <ParamsType> params type parameter.
     * @param <IdType>     id type parameter.
     * @return a java type for specified parameter classes.
     * @see TypeFactory#constructParametricType(Class, Class[])
     */
    public static <ParamsType, IdType> JavaType javaTypeForNamed(final TypeFactory typeFactory,
                                                                 final Class<? extends ParamsType> paramsClass,
                                                                 final Class<? extends IdType> idClass) {
        return typeFactory.constructParametricType(JacksonRequest.class, paramsClass, idClass);
    }

    public static <ParamType, IdType> JavaType javaTypeForPositional(final TypeFactory typeFactory,
                                                                     final Class<? extends Collection> collectionClass,
                                                                     final Class<? extends ParamType> paramClass,
                                                                     final Class<? extends IdType> idClass) {
        final JavaType paramsType = typeFactory.constructCollectionType(collectionClass, paramClass);
        final JavaType idType = typeFactory.constructType(idClass);
        return typeFactory.constructParametricType(JacksonRequest.class, paramsType, idType);
    }

    public static <IdType> JavaType javaTypeForPositional(final TypeFactory typeFactory,
                                                          final Class<? extends Collection> collectionClass,
                                                          final JavaType paramClass,
                                                          final Class<? extends IdType> idClass) {
        final JavaType paramsType = typeFactory.constructCollectionType(collectionClass, paramClass);
        final JavaType idType = typeFactory.constructType(idClass);
        return typeFactory.constructParametricType(JacksonRequest.class, paramsType, idType);
    }

    // ---------------------------------------------------------------------------------------------------------- params

    /**
     * Indicates whether {@value #PROPERTY_NAME_PARAMS} property is a structured value. The {@code isParamsStructured()}
     * method of {@code JacksonRequest} class is overridden to further check if the value is either an instance of
     * {@link ArrayNode}, {@link ObjectNode}, or {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
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

    /**
     * Indicate the current value of {@value #PROPERTY_NAME_ID} property is either {@code string}, {@code number},
     * {@code null}. The {@code isEitherStringNumberOfNull()} method of {@code JacksonRequest} class is overridden to
     * check whether the current value of {@value #PROPERTY_NAME_ID} property is either an instance of {@link TextNode},
     * {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected @AssertTrue boolean isIdEitherStringNumberOfNull() {
        return super.isIdEitherStringNumberOfNull() || isEitherStringNumberOfNull(getId());
    }
}
