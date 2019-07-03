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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import javax.validation.constraints.AssertTrue;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

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
    private static Map<Class<?>, TypeReference> TYPE_REFERENCES;

    private static Map<Class<?>, TypeReference> typeReferences() {
        if (TYPE_REFERENCES == null) {
            TYPE_REFERENCES = new WeakHashMap<>();
        }
        return TYPE_REFERENCES;
    }

    @Deprecated
    @SuppressWarnings({"unchecked"})
    public static <T extends JacksonRequest<? super U, ? super V>, U, V> TypeReference<T> typeReference(
            final Class<? extends T> objectClass) {
        return typeReferences().computeIfAbsent(objectClass, k ->
                new TypeReference<T>() {
                }
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Map<Class<?>, JavaType> JAVA_TYPES;

    private static Map<Class<?>, JavaType> javaTypes() {
        if (JAVA_TYPES == null) {
            JAVA_TYPES = new WeakHashMap<>();
        }
        return JAVA_TYPES;
    }

    @Deprecated
    public static <T extends JacksonRequest<? super U, ? super V>, U, V> JavaType javaType(
            final TypeFactory typeFactory, final Class<? extends T> objectClass, final Class<? extends U> paramsClass,
            final Class<? extends V> idClass) {
        return javaTypes().computeIfAbsent(objectClass, k ->
                typeFactory.constructParametricType(objectClass, paramsClass, idClass)
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Method OF_METHOD;

    static Method ofMethod() {
        if (OF_METHOD == null) {
            try {
                OF_METHOD = RequestObject.class.getDeclaredMethod(
                        "of", Class.class, String.class, String.class, Object.class, Object.class);
                if (!OF_METHOD.isAccessible()) {
                    OF_METHOD.setAccessible(true);
                }
            } catch (final NoSuchMethodException nsme) {
                throw new RuntimeException(nsme);
            }
        }
        return OF_METHOD;
    }

    private static MethodHandle OF_HANDLE;

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

    // ---------------------------------------------------------------------------------------------------------- params

    /**
     * Indicates whether the current value of {@value #PROPERTY_NAME_PARAMS} property is a structured value. The {@code
     * isParamsStructured()} method of {@code JacksonRequest} class is overridden to further check if the value is
     * either an instance of {@link ArrayNode}, {@link ObjectNode}, or {@link NullNode}.
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
     * Indicates whether the current value of {@value #PROPERTY_NAME_ID} property is either {@code string}, {@code
     * number}, {@code null}. The {@code isEitherStringNumberOfNull()} method of {@code JacksonRequest} class is
     * overridden to further check whether the current value of {@value #PROPERTY_NAME_ID} property is either an
     * instance of {@link TextNode}, {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected @AssertTrue boolean isIdEitherStringNumberOfNull() {
        return super.isIdEitherStringNumberOfNull() || isEitherStringNumberOfNull(getId());
    }
}
