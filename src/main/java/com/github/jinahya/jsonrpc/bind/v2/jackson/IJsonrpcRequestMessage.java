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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessage;

import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.getRequestParams;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.setRequestParams;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.evaluatingTrue;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.hasOneThenEvaluateOrFalse;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.hasOneThenMapOrNull;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJsonrpcRequestMessage extends JsonrpcRequestMessage, IJsonrpcMessage {

    @Override
    default boolean hasParams() {
        if (true) {
            return hasOneThenEvaluateOrFalse(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getRequestParams,
                    evaluatingTrue()
            );
        }
        final ContainerNode<?> params = getRequestParams(getClass(), this);
        return params != null && !params.isNull();
    }

    @Override
    default <T> List<T> getParamsAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (true) {
            return hasOneThenMapOrNull(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getRequestParams,
                    params -> {
                        final ObjectMapper mapper = getObjectMapper();
                        final TypeFactory factory = mapper.getTypeFactory();
                        if (params.isArray()) {
                            try {
                                return mapper.convertValue(
                                        params, factory.constructCollectionType(List.class, elementClass));
                            } catch (final IllegalArgumentException iae) {
                                throw new JsonrpcBindException(iae.getCause());
                            }
                        }
                        assert params.isObject();
                        try {
                            return new ArrayList<>(singletonList(mapper.convertValue(params, elementClass)));
                        } catch (final IllegalArgumentException iae) {
                            throw new JsonrpcBindException(iae.getCause());
                        }
                    }
            );
        }
        if (!hasParams()) {
            return null;
        }
        final ContainerNode<?> params = getRequestParams(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (params.isArray()) {
            return mapper.convertValue(
                    params, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
        }
        assert params.isObject();
        final List<T> list = new ArrayList<>(1);
        list.add(mapper.convertValue(params, elementClass));
        return list;
    }

    @Override
    default void setParamsAsArray(final List<?> params) {
        final ObjectMapper mapper = getObjectMapper();
        setRequestParams(getClass(), this, (ArrayNode) ofNullable(params).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (true) {
            return hasOneThenMapOrNull(
                    getClass(),
                    this,
                    IJsonrpcMessageHelper::getRequestParams,
                    params -> {
                        final ObjectMapper mapper = getObjectMapper();
                        try {
                            return mapper.convertValue(params, objectClass);
                        } catch (final IllegalArgumentException iae) {
                            throw new JsonrpcBindException(iae.getCause());
                        }
                    }
            );
        }
        if (!hasParams()) {
            return null;
        }
        final ContainerNode<?> params = getRequestParams(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (params.isObject()) {
            return mapper.convertValue(params, objectClass);
        }
        assert params.isArray();
        if (objectClass.isArray()) {
            return mapper.convertValue(params, objectClass);
        }
        throw new JsonrpcBindException("unable to bind params as an object");
    }

    @Override
    default void setParamsAsObject(final Object params) {
        final ObjectMapper objectMapper = getObjectMapper();
        final JsonNode tree = ofNullable(params)
                .<JsonNode>map(v -> {
                    try {
                        return objectMapper.valueToTree(v);
                    } catch (final IllegalArgumentException iae) {
                        throw new JsonrpcBindException(iae.getCause());
                    }
                })
                .orElse(null);
        if (tree != null && !(tree instanceof ContainerNode)) {
            throw new JsonrpcBindException("illegal value for params: " + params);
        }
        setRequestParams(getClass(), this, (ContainerNode<?>) tree);
    }
}