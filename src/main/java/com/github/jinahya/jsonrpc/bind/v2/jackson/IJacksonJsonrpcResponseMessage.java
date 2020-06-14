package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessage;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcResponseMessageError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.error;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.result;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Collections.list;
import static java.util.Collections.singletonList;
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
    default Boolean getResultAsBoolean(final boolean lenient) {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (result.isBoolean()) {
            return result.booleanValue();
        }
        if (lenient && result.isValueNode()) {
            return result.asBoolean();
        }
        throw new JsonrpcBindException("unable to bind result as a boolean");
    }

    @Override
    default void setResultAsBoolean(final Boolean result) {
        result(getClass(), this, ofNullable(result).map(BooleanNode::valueOf).orElse(null));
    }

    @Override
    default String getResultAsString(final boolean lenient) {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (result.isTextual()) {
            return result.textValue();
        }
        if (lenient && result.isValueNode()) {
            return result.asText();
        }
        throw new JsonrpcBindException("unable to bind result as a string");
    }

    @Override
    default void setResultAsString(final String result) {
        result(getClass(), this, ofNullable(result).map(TextNode::new).orElse(null));
    }

    @Override
    default BigDecimal getResultAsNumber(final boolean lenient) {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (result.isBigDecimal()) { // true in DecimalNode only
            return result.decimalValue();
        }
        if (lenient && result.isNumber()) {
            return result.decimalValue(); // BigDecimal.ZERO if !isNumber()
        }
        throw new JsonrpcBindException("unable to bind result as a number");
    }

    @Override
    default void setResultAsNumber(final BigDecimal result) {
        result(getClass(), this, ofNullable(result).map(DecimalNode::new).orElse(null));
    }

    @Override
    default Long getResultAsLong(final boolean lenient) {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (!lenient && result.isLong()) {
            return result.longValue();
        }
        if (lenient && result.canConvertToLong()) {
            return result.asLong();
        }
        return JsonrpcResponseMessage.super.getResultAsLong(lenient);
    }

    @Override
    default void setResultAsLong(final Long result) {
        result(getClass(), this, ofNullable(result).map(LongNode::new).orElse(null));
    }

    @Override
    default Integer getResultAsInteger(final boolean lenient) {
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        if (!lenient && result.isInt()) {
            return result.intValue();
        }
        if (lenient && result.canConvertToInt()) {
            return result.asInt();
        }
        return JsonrpcResponseMessage.super.getResultAsInteger(lenient);
    }

    @Override
    default void setResultAsInteger(final Integer result) {
        result(getClass(), this, ofNullable(result).map(IntNode::new).orElse(null));
    }

    @Override
    default <T> List<T> getResultAsArray(final Class<T> elementClass, final boolean lenient) {
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
        if (lenient) {
            final T object = getResultAsObject(elementClass, true);
            assert object != null;
            return new ArrayList<>(singletonList(object));
        }
        throw new JsonrpcBindException("unable to bind result as an array");
    }

    @Override
    default void setResultAsArray(final List<?> result) {
        final ObjectMapper mapper = getObjectMapper();
        result(getClass(), this, (BaseJsonNode) ofNullable(result).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass, final boolean lenient) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        final BaseJsonNode result = result(getClass(), this);
        final ObjectMapper mapper = getObjectMapper();
        if (result.isObject()) {
            try {
                return mapper.treeToValue(result, objectClass);
            } catch (final JsonProcessingException jpe) {
                throw new JsonrpcBindException(jpe);
            }
        }
        if (lenient) {
            try {
                return mapper.treeToValue(result, objectClass);
            } catch (final JsonProcessingException jpe) {
                throw new JsonrpcBindException(jpe);
            }
        }
        throw new JsonrpcBindException("unable to bind the value as an object");
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
        try {
            return mapper.treeToValue(error, clazz);
        } catch (final JsonProcessingException jpe) {
            throw new JsonrpcBindException(jpe);
        }
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        final ObjectMapper mapper = getObjectMapper();
        error(getClass(), this, (ObjectNode) ofNullable(error).map(mapper::valueToTree).orElse(null));
    }
}
