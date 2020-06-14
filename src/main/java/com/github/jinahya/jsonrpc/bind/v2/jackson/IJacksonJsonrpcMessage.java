package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
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
    default String getIdAsString(final boolean lenient) {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.isTextual()) {
            return id.textValue();
        }
        if (lenient && id.isValueNode()) {
            return id.asText();
        }
        throw new JsonrpcBindException("unable to bind id as a string");
    }

    @Override
    default void setIdAsString(final String id) {
        id(getClass(), this, ofNullable(id).map(TextNode::new).orElse(null));
    }

    @Override
    default BigInteger getIdAsNumber(final boolean lenient) {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.isBigDecimal()) {
            return id.bigIntegerValue();
        }
        if (lenient && id.isNumber()) {
            return id.bigIntegerValue(); // BigInteger.ZERO of !isNumber()
        }
        if (lenient) {
            try {
                return new BigInteger(id.asText());
            } catch (final NumberFormatException nfe) {
                // empty
            }
        }
        throw new JsonrpcBindException("unable to bind id as a number");
    }

    @Override
    default void setIdAsNumber(final BigInteger id) {
        id(getClass(), this, ofNullable(id).map(BigIntegerNode::new).orElse(null));
    }

    @Override
    default Long getIdAsLong(final boolean lenient) {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.isLong()) {
            return id.longValue();
        }
        if (lenient && id.canConvertToLong()) {
            return id.asLong();
        }
        if (lenient) {
            final long v = id.asLong(); // 0L if representation cannot be converted to a long
            if (v != 0L) {
                return v;
            }
        }
        return JsonrpcMessage.super.getIdAsLong(lenient);
    }

    @Override
    default void setIdAsLong(final Long id) {
        id(getClass(), this, ofNullable(id).map(LongNode::new).orElse(null));
    }

    @Override
    default Integer getIdAsInteger(final boolean lenient) {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.isInt()) {
            return id.intValue();
        }
        if (lenient && id.canConvertToInt()) {
            return id.asInt();
        }
        if (lenient) {
            final int v = id.asInt(); // 0 if representation cannot be converted to int
            if (v != 0) {
                return v;
            }
        }
        return JsonrpcMessage.super.getIdAsInteger(lenient);
    }

    @Override
    default void setIdAsInteger(final Integer id) {
        id(getClass(), this, ofNullable(id).map(IntNode::new).orElse(null));
    }
}
