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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;

import javax.validation.constraints.AssertTrue;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_JSONRPC;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject.PROPERTY_NAME_CODE;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject.PROPERTY_NAME_DATA;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject.PROPERTY_NAME_MESSAGE;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.PROPERTY_NAME_ERROR;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.PROPERTY_NAME_RESULT;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.isEitherStringNumberOfNull;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.requireObjectNode;
import static java.util.Optional.ofNullable;

/**
 * An base class for response objects.
 *
 * @param <ResultType> result type parameter
 * @param <ErrorType>  error type parameter
 * @param <IdType>     id type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonPropertyOrder({PROPERTY_NAME_JSONRPC, PROPERTY_NAME_RESULT, PROPERTY_NAME_ERROR, PROPERTY_NAME_ID})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JacksonResponse<ResultType, ErrorType extends JacksonResponse.JacksonError<?>, IdType>
        extends ResponseObject<ResultType, ErrorType, IdType> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A base class for error objects.
     *
     * @param <DataType> {@value com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject#PROPERTY_NAME_DATA} type
     *                   parameter.
     */
    @JsonPropertyOrder({PROPERTY_NAME_CODE, PROPERTY_NAME_MESSAGE, PROPERTY_NAME_DATA})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JacksonError<DataType> extends ErrorObject<DataType> {

        // -------------------------------------------------------------------------------------------------------------

        /**
         * A class for lazily mapping the {@value com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject#PROPERTY_NAME_DATA}
         * property.
         */
        public static class JacksonServerError extends JacksonError<JsonNode> {

            // ---------------------------------------------------------------------------------------------------------

            /**
             * Creates a new instance whose properties are set from specified json node.
             *
             * @param node the json node from which property values are set; must be an instance of {@link
             *             com.fasterxml.jackson.databind.node.ObjectNode}.
             * @return a new instance.
             */
            public static JacksonServerError of(final JsonNode node) {
                requireObjectNode(node);
                final Integer code = ofNullable(node.get(PROPERTY_NAME_CODE)).map(JsonNode::asInt).orElse(null);
                final String message = ofNullable(node.get(PROPERTY_NAME_MESSAGE)).map(JsonNode::asText).orElse(null);
                final JsonNode data = node.get(PROPERTY_NAME_DATA);
                return of(JacksonServerError.class, code, message, data);
            }

            // ---------------------------------------------------------------------------------------------------------

            /**
             * Creates a new instance.
             */
            public JacksonServerError() {
                super();
            }
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * Returns the method of {@code of(clazz, code, message, data)}.
         *
         * @return the method of {@code of(clazz, code, message, data)}.
         */
        static Method ofMethod() {
            try {
                final Method ofMethod = ErrorObject.class.getDeclaredMethod(
                        "of",
                        Class.class,   // clazz
                        Integer.class, // code
                        String.class,  // message
                        Object.class   // data
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
         * Returns a method handle for {@link #ofMethod()}.
         *
         * @return a method handle for {@link #ofMethod()}.
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
         * Creates a new instance of specified class whose properties are set with specified values.
         *
         * @param clazz   the class of the new instance.
         * @param code    a value for {@link #PROPERTY_NAME_CODE}.
         * @param message a value for {@link #PROPERTY_NAME_MESSAGE}.
         * @param data    a value for {@link #PROPERTY_NAME_DATA}.
         * @param <T>     object type parameter
         * @param <U>     data type parameter
         * @return a new instance.
         */
        static <T extends JacksonError<? super U>, U> T of(final Class<? extends T> clazz, final Integer code,
                                                           final String message, final U data) {
            try {
                return clazz.cast(ofHandle().invokeWithArguments(clazz, code, message, data));
            } catch (final Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * Creates a new instance.
         */
        public JacksonError() {
            super();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a method of {@code of(clazz, jsonrpc, result, error, id}.
     *
     * @return a method of {@code of(clazz, jsonrpc, result, error, id}.
     */
    static Method ofMethod() {
        try {
            final Method ofMethod = ResponseObject.class.getDeclaredMethod(
                    "of",
                    Class.class,       // clazz
                    String.class,      // jsonrpc
                    Object.class,      // result
                    ErrorObject.class, // error
                    Object.class       // id
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
     * Returns a method handle of unreflected value of {@link #ofMethod()}.
     *
     * @return a method handle of unreflected value of {@link #ofMethod()}.
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
     * Creates a new instance of specified class whose properties are set with specified values.
     *
     * @param clazz   the class of the new instance.
     * @param jsonrpc a value for {@link #PROPERTY_NAME_ID} property.
     * @param result  a value for {@link #PROPERTY_NAME_RESULT}.
     * @param error   a value for {@link #PROPERTY_NAME_ERROR}.
     * @param id      a value for {@link #PROPERTY_NAME_ID}.
     * @param <T>     instance type parameter
     * @param <U>     {@link com.github.jinahya.jsonrpc.bind.v2.ResponseObject#PROPERTY_NAME_RESULT} type parameter
     * @param <V>     {@link com.github.jinahya.jsonrpc.bind.v2.ResponseObject#PROPERTY_NAME_ERROR} type parameter
     * @param <W>     {@link com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject#PROPERTY_NAME_ID} type parameter
     * @return a new instance of specified class with specified values for properties.
     */
    static <T extends JacksonResponse<? super U, ? super V, ? super W>, U, V extends JacksonError<?>, W> T of(
            final Class<? extends T> clazz, final String jsonrpc, final U result, final V error, final W id) {
        try {
            return clazz.cast(ofHandle().invokeWithArguments(clazz, jsonrpc, result, error, id));
        } catch (final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public JacksonResponse() {
        super();
    }

    // ---------------------------------------------------------------------------------------------------------- result

    /**
     * Indicates whether the current value of {@link #PROPERTY_NAME_RESULT} property is <i>semantically</i> {@code
     * null}. Overridden to further check whether the the value is an instance of {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected boolean isResultNull() {
        return super.isResultNull() || getResult() instanceof NullNode;
    }

    // -------------------------------------------------------------------------------------------------------------- id

    /**
     * Indicate the current value of {@value com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject#PROPERTY_NAME_ID} property
     * is, <i>semantically</i>, either {@code string}, {@code number}, or {@code null}. The {@code
     * isEitherStringNumberOfNull()} method of {@code JacksonResponse} class is overridden to further check whether the
     * current value of {@value com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject#PROPERTY_NAME_ID} property is either an
     * instance of {@link TextNode}, {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected @AssertTrue boolean isIdEitherStringNumberOfNull() {
        return super.isIdEitherStringNumberOfNull() || isEitherStringNumberOfNull(getId());
    }
}
