package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;

import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.responseError;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.responseResult;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcResponseMessage extends JsonrpcResponseMessage, IJacksonJsonrpcMessage {

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    default boolean hasResult() {
        final BaseJsonNode result = IJacksonJsonrpcObjectHelper.responseResult(getClass(), this);
        return result != null && !result.isNull();
    }

    @Override
    default <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = IJacksonJsonrpcObjectHelper.responseResult(getClass(), this);
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
        responseResult(getClass(), this, (ArrayNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = IJacksonJsonrpcObjectHelper.responseResult(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(result, objectClass);
    }

    @Override
    default void setResultAsObject(final Object result) {
        final ObjectMapper mapper = getObjectMapper();
        responseResult(getClass(), this, (BaseJsonNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------------- error
    @Override
    default boolean hasError() {
        final ObjectNode error = IJacksonJsonrpcObjectHelper.responseError(getClass(), this);
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
        final ObjectNode error = IJacksonJsonrpcObjectHelper.responseError(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(error, clazz);
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        final ObjectMapper mapper = getObjectMapper();
        responseError(getClass(), this, (ObjectNode) ofNullable(error).map(mapper::valueToTree).orElse(null));
    }
}
