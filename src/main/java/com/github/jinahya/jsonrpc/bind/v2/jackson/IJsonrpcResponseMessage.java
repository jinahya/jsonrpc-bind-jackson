package com.github.jinahya.jsonrpc.bind.v2.jackson;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;

import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.getResponseError;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.setResponseError;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.setResponseResult;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.evaluatingTrue;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.hasOneThenEvaluateOrFalse;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.hasOneThenMapOrNull;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJsonrpcResponseMessage extends JsonrpcResponseMessage, IJsonrpcMessage {

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    default boolean hasResult() {
        if (true) {
            return hasOneThenEvaluateOrFalse(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getResponseResult,
                    evaluatingTrue()
            );
        }
        final BaseJsonNode result = IJsonrpcMessageHelper.getResponseResult(getClass(), this);
        return result != null && !result.isNull();
    }

    @Override
    default <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (true) {
            return hasOneThenMapOrNull(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getResponseResult,
                    result -> {
                        final ObjectMapper mapper = getObjectMapper();
                        final TypeFactory factory = mapper.getTypeFactory();
                        if (result.isArray()) {
                            try {
                                return mapper.convertValue(
                                        result, factory.constructCollectionType(List.class, elementClass));
                            } catch (final IllegalArgumentException iae) {
                                throw new JsonrpcBindException(iae.getCause());
                            }
                        }
                        try {
                            return new ArrayList<>(singletonList(mapper.convertValue(result, elementClass)));
                        } catch (final IllegalArgumentException iae) {
                            throw new JsonrpcBindException(iae.getCause());
                        }
                    });
        }
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = IJsonrpcMessageHelper.getResponseResult(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (result.isArray()) {
            return mapper.convertValue(
                    result, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
        }
        final List<T> list = new ArrayList<>(1);
        list.add(mapper.convertValue(result, elementClass));
        return list;
    }

    @Override
    default void setResultAsArray(final List<?> result) {
        final ObjectMapper mapper = getObjectMapper();
        setResponseResult(getClass(), this, (ArrayNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (true) {
            return hasOneThenMapOrNull(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getResponseResult,
                    result -> {
                        final ObjectMapper mapper = getObjectMapper();
                        try {
                            return mapper.convertValue(result, objectClass);
                        } catch (final IllegalArgumentException iae) {
                            throw new JsonrpcBindException(iae.getCause());
                        }
                    }
            );
        }
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = IJsonrpcMessageHelper.getResponseResult(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(result, objectClass);
    }

    @Override
    default void setResultAsObject(final Object result) {
        final ObjectMapper mapper = getObjectMapper();
        setResponseResult(getClass(), this, (BaseJsonNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------------- error
    @Override
    default boolean hasError() {
        if (true) {
            return hasOneThenEvaluateOrFalse(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getResponseError,
                    evaluatingTrue()
            );
        }
        final ObjectNode error = getResponseError(getClass(), this);
        return error != null && !error.isNull();
    }

    @Override
    default boolean isErrorContextuallyValid() {
        return JsonrpcResponseMessage.super.isErrorContextuallyValid();
    }

    @Override
    default <T extends JsonrpcResponseMessageError> T getErrorAs(final Class<T> clazz) {
        requireNonNull(clazz, "clazz is null");
        if (true) {
            return hasOneThenMapOrNull(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getResponseError,
                    error -> {
                        final ObjectMapper mapper = getObjectMapper();
                        try {
                            return mapper.convertValue(error, clazz);
                        } catch (final IllegalArgumentException iae) {
                            throw new JsonrpcBindException(iae.getCause());
                        }
                    }
            );
        }
        if (!hasError()) {
            return null;
        }
        final ObjectNode error = getResponseError(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(error, clazz);
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        final ObjectMapper objectMapper = getObjectMapper();
        setResponseError(getClass(), this, (ObjectNode) ofNullable(error).map(objectMapper::valueToTree).orElse(null));
    }
}
