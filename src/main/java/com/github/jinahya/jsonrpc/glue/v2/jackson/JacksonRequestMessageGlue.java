package com.github.jinahya.jsonrpc.glue.v2.jackson;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.glue.v2.JsonrpcRequestMessageGlue;

public class JacksonRequestMessageGlue
        extends JsonrpcRequestMessageGlue<TextNode, TextNode, ContainerNode<?>, ValueNode>
        implements IJacksonMessageGlue, IJacksonRequestMessageGlue {

}
