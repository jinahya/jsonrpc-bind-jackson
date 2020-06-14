package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2b.AbstractJsonrpcRequestMessage;

import java.util.HashMap;
import java.util.Map;

public class JacksonJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJacksonJsonrpcRequestMessage {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "id=" + id
               + ",params=" + params
               + "}";
    }

    protected ValueNode getId() {
        return id;
    }

    protected void setId(final ValueNode id) {
        this.id = id;
    }

    protected ContainerNode<?> getParams() {
        return params;
    }

    protected void setParams(final ContainerNode<?> params) {
        this.params = params;
    }

    @JsonAnySetter
    protected Object unrecognizedField(final String name, final Object value) {
        return unrecognizedFields().put(name, value);
    }

    protected Map<String, Object> unrecognizedFields() {
        if (unrecognizedFields == null) {
            unrecognizedFields = new HashMap<>();
        }
        return unrecognizedFields;
    }

    private ValueNode id;

    private ContainerNode<?> params;

    private Map<String, Object> unrecognizedFields;
}
