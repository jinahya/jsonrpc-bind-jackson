package com.github.jinahya.jsonrpc.glue.v2.jackson;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.glue.v2.JsonrpcResponseMessageErrorGlue;

class JacksonResponseMessageErrorGlue
        extends JsonrpcResponseMessageErrorGlue<NumericNode, TextNode, BaseJsonNode>
        implements IJacksonResponseMessageErrorGlue {

    private NumericNode code;

    private TextNode message;

    private BaseJsonNode data;
}
