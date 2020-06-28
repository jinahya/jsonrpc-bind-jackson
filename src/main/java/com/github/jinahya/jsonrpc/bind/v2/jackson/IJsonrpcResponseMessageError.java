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
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.setResponseErrorData;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.evaluatingTrue;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.hasOneThenEvaluateOrFalse;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.hasOneThenMapOrNull;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJsonrpcResponseMessageError<S extends IJsonrpcResponseMessageError<S>>
        extends IJsonrpcObject<S>,
                JsonrpcResponseMessageError {

    @Override
    default boolean hasData() {
        return hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseErrorData,
                evaluatingTrue()
        );
    }

    @Override
    @AssertTrue
    default boolean isDataContextuallyValid() {
        return JsonrpcResponseMessageError.super.isDataContextuallyValid();
    }

    @Override
    default <T> List<T> getDataAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseErrorData,
                data -> {
                    final ObjectMapper objectMapper = getObjectMapper();
                    if (data.isArray()) {
                        try {
                            return objectMapper.convertValue(
                                    data,
                                    objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass)
                            );
                        } catch (final IllegalArgumentException iae) {
                            throw new JsonrpcBindException(iae);
                        }
                    }
                    final List<T> list = new ArrayList<>(1);
                    try {
                        list.add(objectMapper.convertValue(data, elementClass));
                    } catch (final IllegalArgumentException iae) {
                        throw new JsonrpcBindException(iae);
                    }
                    return list;
                }
        );
    }

    @Override
    default void setDataAsArray(final List<?> data) {
        final ObjectMapper mapper = getObjectMapper();
        setResponseErrorData(getClass(), this, (ArrayNode) ofNullable(data).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseErrorData,
                data -> {
                    final ObjectMapper objectMapper = getObjectMapper();
                    try {
                        return objectMapper.convertValue(data, objectClass);
                    } catch (final IllegalArgumentException iae) {
                        throw new JsonrpcBindException(iae);
                    }
                }
        );
    }

    @Override
    default void setDataAsObject(final Object data) {
        setResponseErrorData(
                getClass(),
                this,
                (BaseJsonNode) ofNullable(data)
                        .map(v -> {
                            try {
                                return getObjectMapper().valueToTree(v);
                            } catch (final IllegalArgumentException iae) {
                                throw new JsonrpcBindException(iae);
                            }
                        })
                        .orElse(null)
        );
    }
}
