package com.github.jinahya.jsonrpc.glue.v2.jackson;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.glue.v2.IJsonrpcRequestMessageGlue;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;

public interface IJacksonRequestMessageGlue
        extends IJsonrpcRequestMessageGlue<TextNode, TextNode, ContainerNode<?>, ValueNode> {

    @Override
    default @AssertTrue boolean isMethodContextuallyValid() {
        if (isMethodReservedForRpcInternal()) {
            return false;
        }
        return !getMethod().asText().isEmpty();
    }

    @Override
    default @AssertFalse boolean isMethodReservedForRpcInternal() {
        return getMethod().asText().startsWith(METHOD_NAME_PREFIX_RESERVED_FOR_RPC_INTERNAL);
    }

    @Override
    default @AssertTrue boolean isParamsContextuallyValid() {
        return getParams() == null || getParams() instanceof ArrayNode || getParams() instanceof ObjectNode;
    }
}
