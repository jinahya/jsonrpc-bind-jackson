package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.github.jinahya.jsonrpc.bind.v2b.JsonrpcMessage;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;

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
        final ValueNode id = id(getClass(), this);
        return id.asText();
    }

    @Override
    default void setIdAsString(final String id) {
        id(getClass(), this, ofNullable(id).map(TextNode::new).orElse(null));
    }

    @Override
    default BigDecimal getIdAsNumber() {
        if (!hasId()) {
            return null;
        }
        final ValueNode id = id(getClass(), this);
        if (id.isNumber()) {
            return id.decimalValue();
        }
        if (id.isTextual()) {
            try {
                return new BigDecimal(id.asText());
            } catch (final NumberFormatException nfe) {
                // empty
            }
        }
        throw new JsonrpcBindException("unable to bind id as a number");
    }

    @Override
    default void setIdAsNumber(final BigDecimal id) {
        id(getClass(), this, ofNullable(id).map(DecimalNode::valueOf).orElse(null));
    }
}
