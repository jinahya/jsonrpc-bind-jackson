package com.github.jinahya.jsonrpc.glue.v2.jackson;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.glue.v2.JsonrpcResponseMessageGlue;

class JacksonResponseMessageGlue
        extends JsonrpcResponseMessageGlue<TextNode, BaseJsonNode, ObjectNode, ValueNode>
        implements IJacksonResponseMessageGlue {

}
