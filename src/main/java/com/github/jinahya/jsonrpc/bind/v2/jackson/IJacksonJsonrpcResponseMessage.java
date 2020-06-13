package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessage;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessageError;

import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
        return result == null || result.isNull();
    }

    @Override
    default Boolean getResultAsBoolean() {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        return result.asBoolean();
    }

    @Override
    default void setResultAsBoolean(final Boolean result) {
        result(getClass(), this, ofNullable(result).map(BooleanNode::valueOf).orElse(null));
    }

    @Override
    default String getResultAsString() {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (result.isValueNode()) {
            return result(getClass(), this).asText();
        }
        return null;
    }

    @Override
    default void setResultAsString(final String result) {
        result(getClass(), this, ofNullable(result).map(TextNode::new).orElse(null));
    }

    @Override
    default BigDecimal getResultAsNumber() {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (result.isNumber()) {
            return result(getClass(), this).decimalValue();
        }
        return null;
    }

    @Override
    default void setResultAsNumber(final BigDecimal result) {
        result(getClass(), this, ofNullable(result).map(DecimalNode::new).orElse(null));
    }

    @Override
    default <T> List<T> getResultAsList(final Class<T> elementClass) {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (result instanceof ContainerNode) {
            if (result instanceof ObjectNode) {
                return ofNullable(getResultAsObject(elementClass))
                        .map(Collections::singletonList)
                        .map(ArrayList::new)
                        .orElse(null);
            }
            assert result instanceof ArrayNode;
            return mapper.convertValue(
                    result, mapper.getTypeFactory().constructCollectionType(List.class, elementClass));
        }
        if (result instanceof ValueNode) {
            final List<T> list = new ArrayList<>(1);
            final T value;
            try {
                value = mapper.treeToValue(result, elementClass);
            } catch (final JsonProcessingException jpe) {
                throw new UncheckedIOException(jpe);
            }
            list.add(value);
            return list;
        }
        return null;
    }

    @Override
    default void setResultAsList(final List<?> result) {
        result(getClass(), this, (BaseJsonNode) ofNullable(result).map(getObjectMapper()::valueToTree).orElse(null));
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        try {
            return getObjectMapper().treeToValue(result, objectClass);
        } catch (final JsonProcessingException jpe) {
            throw new UncheckedIOException(jpe);
        }
    }

    @Override
    default void setResultAsObject(final Object result) {
        result(getClass(), this, (BaseJsonNode) ofNullable(result).map(getObjectMapper()::valueToTree).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------------- error
    @Override
    default boolean hasError() {
        final ObjectNode error = error(getClass(), this);
        return error == null || error.isNull();
    }

    //TODO: remove
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
        try {
            return getObjectMapper().treeToValue(error(getClass(), this), clazz);
        } catch (final JsonProcessingException jpe) {
            throw new UncheckedIOException(jpe);
        }
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        error(getClass(), this, (ObjectNode) ofNullable(error).map(getObjectMapper()::valueToTree).orElse(null));
    }
}
