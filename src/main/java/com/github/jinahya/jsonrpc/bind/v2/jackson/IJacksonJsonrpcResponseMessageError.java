package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessageError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.errorData;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcResponseMessageError extends JsonrpcResponseMessageError {

    @Override
    default boolean hasData() {
        final BaseJsonNode data = errorData(getClass(), this);
        return data != null && data.isNull();
    }

    @Override
    default Boolean getDataAsBoolean(final boolean lenient) {
        if (!hasData()) {
            return null;
        }
        final BaseJsonNode data = errorData(getClass(), this);
        if (data.isBoolean()) {
            return data.booleanValue();
        }
        if (lenient && data.isValueNode()) {
            return data.asBoolean();
        }
        throw new JsonrpcBindException("unable to present data as a boolean");
    }

    @Override
    default void setDataAsBoolean(final Boolean data) {
        errorData(getClass(), this, ofNullable(data).map(BooleanNode::valueOf).orElse(null));
    }

    @Override
    default String getDataAsString(final boolean lenient) {
        if (!hasData()) {
            return null;
        }
        final BaseJsonNode data = errorData(getClass(), this);
        if (data.isTextual()) {
            return data.textValue();
        }
        if (lenient && data.isValueNode()) {
            return data.asText();
        }
        throw new JsonrpcBindException("unable to present data as a string");
    }

    @Override
    default void setDataAsString(final String data) {
        errorData(getClass(), this, ofNullable(data).map(TextNode::new).orElse(null));
    }

    @Override
    default BigDecimal getDataAsNumber(final boolean lenient) {
        if (!hasData()) {
            return null;
        }
        final BaseJsonNode data = errorData(getClass(), this);
        if (data.isBigDecimal()) {
            return data.decimalValue();
        }
        if (lenient && data.isNumber()) {
            return data.decimalValue();
        }
        throw new JsonrpcBindException("unable to present the value as a number");
    }

    @Override
    default void setDataAsNumber(final BigDecimal data) {
        errorData(getClass(), this, ofNullable(data).map(DecimalNode::valueOf).orElse(null));
    }

    @Override
    default <T> List<T> getDataAsArray(final Class<T> elementClass, final boolean lenient) {
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
        if (lenient) {
            final T object = getDataAsObject(elementClass, true);
            assert object != null;
            return new ArrayList<>(singletonList(object));
        }
        throw new JsonrpcBindException("unable to parse data as an array");
    }

    @Override
    default void setDataAsArray(final List<?> data) {
        final ObjectMapper mapper = getObjectMapper();
        errorData(getClass(), this, (BaseJsonNode) ofNullable(data).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getDataAsObject(final Class<T> objectClass, final boolean lenient) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasData()) {
            return null;
        }
        final BaseJsonNode data = errorData(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (data.isObject()) {
            try {
                return mapper.treeToValue(errorData(getClass(), this), objectClass);
            } catch (final JsonProcessingException jpe) {
                throw new JsonrpcBindException(jpe);
            }
        }
        if (lenient) {
            try {
                return mapper.treeToValue(data, objectClass);
            } catch (final JsonProcessingException jpe) {
                throw new JsonrpcBindException(jpe);
            }
        }
        throw new JsonrpcBindException("unable to present data as an object");
    }

    @Override
    default void setDataAsObject(final Object data) {
        final ObjectMapper mapper = getObjectMapper();
        errorData(getClass(), this, (BaseJsonNode) ofNullable(data).map(mapper::valueToTree).orElse(null));
    }
}
