package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

import javax.validation.constraints.AssertTrue;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonUtils.isEitherTextNumberOrNull;

public class LazyMappedResponse extends JacksonServerResponse<JsonNode, ErrorObject<JsonNode>, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------
    @AssertTrue(message = "a non-null id must be either TextNode, NumericNode, or NullNode")
    private boolean isIdEitherTextNumberOrNull() {
        final ValueNode id = getId();
        return id == null || isEitherTextNumberOrNull(id);
    }

    // -----------------------------------------------------------------------------------------------------------------
}
