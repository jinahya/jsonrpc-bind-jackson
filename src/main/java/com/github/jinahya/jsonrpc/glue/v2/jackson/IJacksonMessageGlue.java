package com.github.jinahya.jsonrpc.glue.v2.jackson;

import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.glue.v2.IJsonrpcMessageGlue;

import javax.validation.constraints.AssertTrue;

public interface IJacksonMessageGlue extends IJsonrpcMessageGlue<TextNode, ValueNode>, IJacksonObjectGlue {

    @Override
    default @AssertTrue boolean isJsonrpcContextuallyValid() {
        return getJsonrpc().asText().equals(MEMBER_VALUE_JSONRPC);
    }

    @Override
    default @AssertTrue boolean isIdContextuallyValid() {
        return getId() == null
               || getId() instanceof TextNode || getId() instanceof NumericNode || getId() instanceof NullNode;
    }
}
