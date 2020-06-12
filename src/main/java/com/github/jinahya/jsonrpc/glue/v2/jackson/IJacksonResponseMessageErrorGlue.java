package com.github.jinahya.jsonrpc.glue.v2.jackson;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.glue.v2.IJsonrpcResponseMessageErrorGlue;

import javax.validation.constraints.AssertTrue;

interface IJacksonResponseMessageErrorGlue
        extends IJsonrpcResponseMessageErrorGlue<NumericNode, TextNode, BaseJsonNode> {

    @Override
    default @AssertTrue boolean isCodeContextuallyValid() {
        return getCode() == null
               || (!(getCode() instanceof DecimalNode)
                   && !(getCode() instanceof DoubleNode)
                   && !(getCode() instanceof FloatNode));
    }

    @Override
    default @AssertTrue boolean isMessageContextuallyValid() {
        return true;
    }

    @Override
    default @AssertTrue boolean isDataContextuallyValid() {
        return true;
    }
}
