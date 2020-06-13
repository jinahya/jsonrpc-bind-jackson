package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcMessage;

import javax.validation.constraints.AssertTrue;
import java.math.BigInteger;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJacksonJsonrpcObjectHelper.id;
import static java.util.Optional.ofNullable;

interface IJacksonJsonrpcMessage extends JsonrpcMessage, IJacksonJsonrpcObject {

    @Override
    default boolean hasId() {
        final ValueNode id = id(getClass(), this);
        return id != null && !id.isNull();
    }

    @Override
    default @AssertTrue boolean isIdContextuallyValid() {
        if (!hasId()) {
            return true;
        }
        final ValueNode id = id(getClass(), this);
        return id.isTextual() || id.isIntegralNumber();
    }

    @Override
    default String getIdAsString() {
        if (!hasId()) {
            return null;
        }
        return id(getClass(), this).asText();
    }

    @Override
    default void setIdAsString(final String id) {
        id(getClass(), this, ofNullable(id).map(TextNode::new).orElse(null));
    }

    @Override
    default BigInteger getIdAsNumber() {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        assert id != null;
        assert !id.isNull();
        if (id.isNumber()) {
            return id.bigIntegerValue(); // BigInteger.ZERO of !isNumber()
        }
        try {
            return new BigInteger(id.asText());
        } catch (final NumberFormatException nfe) {
            // empty
        }
        return null;
    }

    @Override
    default void setIdAsNumber(final BigInteger id) {
        id(getClass(), this, ofNullable(id).map(BigIntegerNode::new).orElse(null));
    }

    @Override
    default Long getIdAsLong() {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.canConvertToLong()) {
            return id.asLong();
        }
        return JsonrpcMessage.super.getIdAsLong();
    }

    @Override
    default void setIdAsLong(final Long id) {
        id(getClass(), this, ofNullable(id).map(LongNode::new).orElse(null));
    }

    @Override
    default Integer getIdAsInteger() {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.canConvertToInt()) {
            return id.asInt();
        }
        return JsonrpcMessage.super.getIdAsInteger();
    }

    @Override
    default void setIdAsInteger(final Integer id) {
        id(getClass(), this, ofNullable(id).map(IntNode::new).orElse(null));
    }
}
