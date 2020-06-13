package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2b.AbstractJsonrpcRequestMessage;

import static java.util.Objects.requireNonNull;

public class JacksonJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJacksonJsonrpcRequestMessage {

    public JacksonJsonrpcRequestMessage(final ObjectMapper mapper) {
        super();
        this.mapper = requireNonNull(mapper, "mapper is null");
    }

    public JacksonJsonrpcRequestMessage() {
        this(new ObjectMapper());
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + "id=" + id
               + ",params=" + params
               + "}";
    }

    private ValueNode getId() {
        return id;
    }

    private void setId(final ValueNode id) {
        this.id = id;
    }

    private ContainerNode<?> getParams() {
        return params;
    }

    private void setParams(final ContainerNode<?> params) {
        this.params = params;
    }

    private final ObjectMapper mapper;

    private ValueNode id;

    private ContainerNode<?> params;
}
