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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import javax.validation.constraints.AssertTrue;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_JSONRPC;
import static com.github.jinahya.jsonrpc.bind.v2.RequestObject.PROPERTY_NAME_METHOD;
import static com.github.jinahya.jsonrpc.bind.v2.RequestObject.PROPERTY_NAME_PARAMS;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.isEitherStringNumberOfNull;

/**
 * An base class for request objects.
 *
 * @param <ParamsType> {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_PARAMS} type parameter
 * @param <IdType>     {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_ID} type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonPropertyOrder({PROPERTY_NAME_JSONRPC, PROPERTY_NAME_METHOD, PROPERTY_NAME_PARAMS, PROPERTY_NAME_ID})
@JsonInclude(JsonInclude.Include.NON_NULL) // for notification
public class JacksonRequest<ParamsType, IdType> extends RequestObject<ParamsType, IdType> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a method of {@code of(clazz, jsonrpc, method, params, id)}.
     *
     * @return a method of {@code of(clazz, jsonrpc, method, params, id)}.
     */
    static Method ofMethod() {
        try {
            final Method ofMethod = RequestObject.class.getDeclaredMethod(
                    "of",
                    Class.class,  // clazz
                    String.class, // jsonrpc
                    String.class, // method
                    Object.class, // params
                    Object.class  // id
            );
            if (!ofMethod.isAccessible()) {
                ofMethod.setAccessible(true);
            }
            return ofMethod;
        } catch (final NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
    }

    private static MethodHandle OF_HANDLE;

    /**
     * Returns a method handle unreflected from {@link #ofMethod()}.
     *
     * @return a method handle unreflected from {@link #ofMethod()}.
     */
    static MethodHandle ofHandle() {
        if (OF_HANDLE == null) {
            try {
                OF_HANDLE = MethodHandles.lookup().unreflect(ofMethod());
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        }
        return OF_HANDLE;
    }

    /**
     * Creates a new instance of specified class whose properties are set with give values.
     *
     * @param clazz   the class of the object to create.
     * @param jsonrpc a value for {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_JSONRPC}
     *                property.
     * @param method  a value for {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_METHOD}
     *                property.
     * @param params  a value for {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_ID} property.
     * @param id      a value for {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_VALUE_JSONRPC}
     *                property.
     * @param <T>     object type parameter
     * @param <U>     {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_PARAMS} type parameter
     * @param <V>     {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_PARAMS} type parameter
     * @return a new instance.
     */
    static <T extends JacksonRequest<U, V>, U, V> T of(
            final Class<? extends T> clazz, final String jsonrpc, final String method, final U params, final V id) {
        try {
            return clazz.cast(ofHandle().invokeWithArguments(clazz, jsonrpc, method, params, id));
        } catch (final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public JacksonRequest() {
        super();
    }

    // ---------------------------------------------------------------------------------------------------------- params
//
//    /**
//     * Indicates whether the current value of {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_PARAMS}
//     * property is <i>semantically</i> {@code null}. The {@code isParamsNull()} method of {@code JacksonRequest} class
//     * is overridden to further check if the current value of {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_PARAMS}
//     * property is an instance of {@link NullNode}.
//     *
//     * @return {@inheritDoc}
//     */
//    @Override
//    protected boolean isParamsNull() {
//        return super.isParamsNull() || getParams() instanceof NullNode;
//    }

    /**
     * Indicates whether the current value of {@value com.github.jinahya.jsonrpc.bind.v2.RequestObject#PROPERTY_NAME_PARAMS}
     * property is a structured value. The {@code isParamsStructured()} method of {@code JacksonRequest} class is
     * overridden to further check if the value is either an instance of {@link ArrayNode}, {@link ObjectNode}, or
     * {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected @AssertTrue boolean isParamsStructured() {
        if (super.isParamsStructured()) {
            return true;
        }
        final ParamsType params = getParams();
        return params instanceof ArrayNode || params instanceof ObjectNode || params instanceof NullNode;
    }

    // -------------------------------------------------------------------------------------------------------------- id

    /**
     * Indicates whether the current value of {@value #PROPERTY_NAME_ID} property is, <i>semantically</i>, either {@code
     * string}, {@code number}, or {@code null}. The {@code isEitherStringNumberOfNull()} method of {@code
     * JacksonRequest} class is overridden to further check whether the current value of {@value #PROPERTY_NAME_ID}
     * property is either an instance of {@link TextNode}, {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected @AssertTrue boolean isIdEitherStringNumberOfNull() {
        return super.isIdEitherStringNumberOfNull() || isEitherStringNumberOfNull(getId());
    }
}
