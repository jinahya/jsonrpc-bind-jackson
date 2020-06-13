package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcRequestMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.params;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.mapper;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.params;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcRequestMessage extends JsonrpcRequestMessage, IJacksonJsonrpcMessage {

    @Override
    default <T> List<T> getParamsAsList(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        final ContainerNode<?> params = params(getClass(), this);
        if (params == null || params.isNull()) {
            return null;
        }
        if (params instanceof ObjectNode) {
            return ofNullable(getParamsAsObject(elementClass))
                    .map(Collections::singletonList)
                    .map(ArrayList::new)
                    .orElse(null);
        }
        assert params instanceof ArrayNode;
        final ObjectMapper mapper = mapper(getClass(), this);
        return mapper.convertValue(params, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
    }

    @Override
    default void setParamsAsList(final List<?> params) {
        final ObjectMapper mapper = mapper(getClass(), this);
        params(getClass(), this, (ContainerNode<?>) ofNullable(params).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        final ObjectMapper mapper = mapper(getClass(), this);
        return ofNullable(params(getClass(), this))
                .map(v -> mapper.convertValue(v, objectClass))
                .orElse(null);
    }

    @Override
    default void setParamsAsObject(final Object params) {
        final ObjectMapper mapper = mapper(getClass(), this);
        params(getClass(), this, (ContainerNode<?>) ofNullable(params).map(mapper.valueToTree(params)).orElse(null));
    }
}
