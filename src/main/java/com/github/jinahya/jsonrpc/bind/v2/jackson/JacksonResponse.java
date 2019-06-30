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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

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
public class JacksonResponse<ResultType, ErrorType extends ErrorObject<?>, IdType>
        extends ResponseObject<ResultType, ErrorType, IdType> {

    // -----------------------------------------------------------------------------------------------------------------
    @JsonPropertyOrder({PROPERTY_NAME_CODE, PROPERTY_NAME_MESSAGE, PROPERTY_NAME_DATA})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JacksonError<DataType> extends ErrorObject<DataType> {

    }

    public static class JacksonServerError extends JacksonError<JsonNode> {

    }

    // -----------------------------------------------------------------------------------------------------------------
//    static JavaType javaTypeForNamed(final TypeFactory typeFactory, final JavaType resultType, final JavaType errorType,
//                                final JavaType idType) {
//        return typeFactory.constructParametricType(JacksonResponse.class, resultType, errorType, idType);
//    }

    public static <ResultType, ErrorType extends ErrorObject<?>, IdType> JavaType javaTypeFor(
            final TypeFactory typeFactory, final Class<? extends ResultType> resultClass,
            final Class<? extends ErrorType> errorClass, final Class<? extends IdType> idClass) {
        return typeFactory.constructParametricType(ResponseObject.class, resultClass, errorClass, idClass);
    }

    public static <ResultType, IdType> JavaType javaTypeForResult(
            final TypeFactory typeFactory, final Class<? extends ResultType> resultClass,
            final Class<? extends IdType> idClass) {
        return typeFactory.constructParametricType(
                JacksonResponse.class, resultClass, JacksonServerError.class, idClass);
    }

    public static <ErrorType extends ErrorObject<?>, IdType> JavaType javaTypeForError(
            final TypeFactory typeFactory, final Class<? extends ErrorType> errorClass,
            final Class<? extends IdType> idClass) {
        return typeFactory.constructParametricType(JacksonResponse.class, JsonNode.class, errorClass, idClass);
    }

    // ---------------------------------------------------------------------------------------------------------- result

    /**
     * Indicates whether the current value of {@value #PROPERTY_NAME_RESULT} property is <i>semantically</i> {@code
     * null}. Overridden to check whether the current value of {@value #PROPERTY_NAME_RESULT} property is an instance of
     * {@link NullNode}.
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
