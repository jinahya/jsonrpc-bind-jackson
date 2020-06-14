package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcRequestMessage;

import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.params;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcRequestMessage extends JsonrpcRequestMessage, IJacksonJsonrpcMessage {

    @Override
    default boolean hasParams() {
        final ContainerNode<?> params = params(getClass(), this);
        return params != null && !params.isNull();
    }

    @Override
    default <T> List<T> getParamsAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasParams()) {
            return null;
        }
        final ContainerNode<?> params = params(getClass(), this);
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
        params(getClass(), this, (ArrayNode) ofNullable(params).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasParams()) {
            return null;
        }
        final ContainerNode<?> params = params(getClass(), this);
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
        final ObjectMapper mapper = getObjectMapper();
        params(getClass(), this, (ContainerNode<?>) ofNullable(params).map(mapper.valueToTree(params)).orElse(null));
    }
}
