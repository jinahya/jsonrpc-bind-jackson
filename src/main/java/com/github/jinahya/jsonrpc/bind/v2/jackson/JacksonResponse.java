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

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject.PROPERTY_NAME_JSONRPC;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject.PROPERTY_NAME_CODE;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject.PROPERTY_NAME_DATA;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject.PROPERTY_NAME_MESSAGE;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.PROPERTY_NAME_ERROR;
import static com.github.jinahya.jsonrpc.bind.v2.ResponseObject.PROPERTY_NAME_RESULT;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonObjects.isEitherStringNumberOfNull;

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
            public static <T extends JacksonServerError> T of(final Class<? extends T> clazz, final ObjectNode node) {
                if (clazz == null) {
                    throw new NullPointerException("clazz is null");
                }
                if (node == null) {
                    throw new NullPointerException("node is null");
                }
                return of(clazz, node.get(PROPERTY_NAME_CODE).asInt(), node.get(PROPERTY_NAME_MESSAGE).asText(),
                          node.get(PROPERTY_NAME_DATA));
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
