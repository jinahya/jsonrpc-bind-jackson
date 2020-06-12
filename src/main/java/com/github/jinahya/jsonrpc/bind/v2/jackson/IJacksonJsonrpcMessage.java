package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2a.IJsonrpcMessage;
import com.github.jinahya.jsonrpc.glue.v2.jackson.IJacksonObjectGlue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;

import static java.util.Optional.ofNullable;

public interface IJacksonJsonrpcMessage extends IJsonrpcMessage, IJacksonObjectGlue {

    // --------------------------------------------------------------------------------------------------------- jsonrpc
    @Override
    default @Pattern(regexp = "2\\.0") @NotNull String getJsonrpc() {
        return ofNullable(IJacksonJsonrpcObjectHelper.glue(getClass(), this).getJsonrpc()).map(JsonNode::asText).orElse(null);
    }

    @Override
    default void setJsonrpc(final String jsonrpc) {
        IJacksonJsonrpcObjectHelper.glue(getClass(), this).setJsonrpc(ofNullable(jsonrpc).map(TextNode::new).orElse(null));
    }

    // -------------------------------------------------------------------------------------------------------------- id
    @Override
    default String getIdAsString() {
        return ofNullable(IJacksonJsonrpcObjectHelper.glue(getClass(), this).getId())
                .map(JsonNode::asText)
                .orElse(null);
    }

    @JsonProperty(PROPERTY_NAME_ID)
    @Override
    default void setIdAsString(final String id) {
        IJacksonJsonrpcObjectHelper.glue(getClass(), this).setJsonrpc(ofNullable(id).map(TextNode::new).orElse(null));
    }

    @Override
    default Integer getIdAsInteger() {
        return ofNullable(IJacksonJsonrpcObjectHelper.glue(getClass(), this).getId())
                .map(JsonNode::asInt)
                .orElse(null);
    }

    @JsonProperty(PROPERTY_NAME_ID)
    @Override
    default void setIdAsInteger(final Integer id) {
        IJacksonJsonrpcObjectHelper.glue(getClass(), this).setId(ofNullable(id).map(IntNode::new).orElse(null));
    }

    @Override
    default Long getIdAsLong() {
        return ofNullable(IJacksonJsonrpcObjectHelper.glue(getClass(), this).getId())
                .map(JsonNode::asLong)
                .orElse(null);
    }

    @JsonProperty(PROPERTY_NAME_ID)
    @Override
    default void setIdAsLong(final Long id) {
        IJacksonJsonrpcObjectHelper.glue(getClass(), this).setId(ofNullable(id).map(LongNode::new).orElse(null));
    }

    @Override
    default BigInteger getIdAsBigInteger() {
        return ofNullable(IJacksonJsonrpcObjectHelper.glue(getClass(), this).getId())
                .map(ValueNode::bigIntegerValue)
                .orElse(null);
    }

    @JsonProperty(PROPERTY_NAME_ID)
    @Override
    default void setIdAsBigInteger(final BigInteger id) {
        IJacksonJsonrpcObjectHelper.glue(getClass(), this).setId(ofNullable(id).map(BigIntegerNode::new).orElse(null));
    }
}
