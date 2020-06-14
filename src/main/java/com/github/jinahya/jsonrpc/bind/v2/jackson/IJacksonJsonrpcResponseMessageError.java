package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessageError;

import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.errorData;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcResponseMessageError extends JsonrpcResponseMessageError {

    @Override
    default boolean hasData() {
        final BaseJsonNode data = errorData(getClass(), this);
        return data != null && !data.isNull();
    }

    @Override
    default <T> List<T> getDataAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasData()) {
            return null;
        }
        final BaseJsonNode data = errorData(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (data.isArray()) {
            return mapper.convertValue(
                    data, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
        }
        final List<T> list = new ArrayList<>(1);
        list.add(mapper.convertValue(data, elementClass));
        return list;
    }

    @Override
    default void setDataAsArray(final List<?> data) {
        final ObjectMapper mapper = getObjectMapper();
        errorData(getClass(), this, (ArrayNode) ofNullable(data).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasData()) {
            return null;
        }
        final BaseJsonNode data = errorData(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(data, objectClass);
    }

    @Override
    default void setDataAsObject(final Object data) {
        final ObjectMapper mapper = getObjectMapper();
        errorData(getClass(), this, (BaseJsonNode) ofNullable(data).map(mapper::valueToTree).orElse(null));
    }
}
