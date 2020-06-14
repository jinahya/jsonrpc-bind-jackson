package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessage;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessageError;

import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.error;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.result;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcResponseMessage extends JsonrpcResponseMessage, IJacksonJsonrpcMessage {

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    default boolean hasResult() {
        final BaseJsonNode result = result(getClass(), this);
        return result != null && !result.isNull();
    }

    @Override
    default <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
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
        result(getClass(), this, (ArrayNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(result, objectClass);
    }

    @Override
    default void setResultAsObject(final Object result) {
        final ObjectMapper mapper = getObjectMapper();
        result(getClass(), this, (BaseJsonNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------------- error
    @Override
    default boolean hasError() {
        final ObjectNode error = error(getClass(), this);
        return error != null && !error.isNull();
    }

    @Override
    default boolean isErrorContextuallyValid() {
        return JsonrpcResponseMessage.super.isErrorContextuallyValid();
    }

    @Override
    default <T extends JsonrpcResponseMessageError> T getErrorAs(final Class<T> clazz) {
        requireNonNull(clazz, "clazz is null");
        if (!hasError()) {
            return null;
        }
        final ObjectNode error = error(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(error, clazz);
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        final ObjectMapper mapper = getObjectMapper();
        error(getClass(), this, (ObjectNode) ofNullable(error).map(mapper::valueToTree).orElse(null));
    }
}
