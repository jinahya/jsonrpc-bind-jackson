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
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        private static Method OF_METHOD;

        private static Method ofMethod() {
            if (OF_METHOD == null) {
                try {
                    OF_METHOD = ErrorObject.class.getDeclaredMethod(
                            "of", Class.class, Integer.class, String.class, Object.class);
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

        private static MethodHandle ofHandle() {
            if (OF_HANDLE == null) {
                try {
                    OF_HANDLE = MethodHandles.lookup().unreflect(ofMethod());
                } catch (final ReflectiveOperationException roe) {
                    throw new RuntimeException(roe);
                }
            }
            return OF_HANDLE;
        }

        // -------------------------------------------------------------------------------------------------------------

        /**
         * A class for lazily mapping the {@value com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject#PROPERTY_NAME_DATA}
         * property.
         */
        public static class JacksonServerError extends JacksonError<JsonNode> {

            // ---------------------------------------------------------------------------------------------------------
            public static <T extends JacksonServerError> T of(final Class<? extends T> clazz, final ObjectNode node) {
                if (clazz == null) {
                    throw new NullPointerException("clazz is null");
                }
                if (node == null) {
                    throw new NullPointerException("node is null");
                }
                final Integer code = ofNullable(node.get(PROPERTY_NAME_CODE)).map(JsonNode::asInt).orElse(null);
                final String message = ofNullable(node.get(PROPERTY_NAME_MESSAGE)).map(JsonNode::asText).orElse(null);
                final JsonNode data = node.get(PROPERTY_NAME_DATA);
//                return of(clazz, node.get(PROPERTY_NAME_CODE).asInt(), node.get(PROPERTY_NAME_MESSAGE).asText(),
//                          node.get(PROPERTY_NAME_DATA));
                try {
                    return clazz.cast(JacksonError.ofHandle().invokeWithArguments(code, message, data));
                } catch (final Throwable thrown) {
                    throw new RuntimeException(thrown);
                }
            }

            public static <T extends JacksonServerError> T of(final Class<? extends T> clazz, final JsonNode node) {
                if (clazz == null) {
                    throw new NullPointerException("clazz is null");
                }
                if (node == null) {
                    throw new NullPointerException("node is null");
                }
                final JsonNodeType type = node.getNodeType();
                if (type != JsonNodeType.OBJECT) {
                    throw new IllegalArgumentException(
                            "node(" + node + ").type(" + type + ") != " + JsonNodeType.OBJECT);
                }
                return of(clazz, (ObjectNode) node);
            }

            // ---------------------------------------------------------------------------------------------------------
            public static JacksonServerError of(final ObjectNode node) {
                if (node == null) {
                    throw new NullPointerException("node is null");
                }
                return of(JacksonServerError.class, node);
            }

            public static JacksonServerError of(final JsonNode node) {
                if (node == null) {
                    throw new NullPointerException("node is null");
                }
                final JsonNodeType type = node.getNodeType();
                if (type != JsonNodeType.OBJECT) {
                    throw new IllegalArgumentException(
                            "node(" + node + ").type(" + type + ") != " + JsonNodeType.OBJECT);
                }
                return of((ObjectNode) node);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Method OF_METHOD;

    static Method ofMethod() {
        if (OF_METHOD == null) {
            try {
                OF_METHOD = ResponseObject.class.getDeclaredMethod(
                        "of", Class.class, String.class, Object.class, ErrorObject.class, Object.class);
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

    // ---------------------------------------------------------------------------------------------------------- result

    /**
     * Indicates whether the current value of {@value #PROPERTY_NAME_RESULT} property is <i>semantically</i> {@code
     * null}. Overridden to further check whether the current value of {@value #PROPERTY_NAME_RESULT} property is an
     * instance of {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected boolean isResultSemanticallyNull() {
        return super.isResultSemanticallyNull() || getResult() instanceof NullNode;
    }

    // -------------------------------------------------------------------------------------------------------------- id

    /**
     * Indicate the current value of {@value #PROPERTY_NAME_ID} property is either {@code string}, {@code number},
     * {@code null}. The {@code isEitherStringNumberOfNull()} method of {@code JacksonResponse} class is overridden to
     * further check whether the current value of {@value #PROPERTY_NAME_ID} property is either an instance of {@link
     * TextNode}, {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@inheritDoc}
     */
    @Override
    protected @AssertTrue boolean isIdEitherStringNumberOfNull() {
        return super.isIdEitherStringNumberOfNull() || isEitherStringNumberOfNull(getId());
    }
}
