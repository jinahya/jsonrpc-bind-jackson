package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcRequestMessage;

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
               + ",unrecognizedFields=" + unrecognizedFields
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

    private ValueNode id;

    private ContainerNode<?> params;

    @JsonAnySetter
    protected Object putUnrecognizedField(final String name, final Object value) {
        return getUnrecognizedFields().put(name, value);
    }

    public Map<String, Object> getUnrecognizedFields() {
        if (unrecognizedFields == null) {
            unrecognizedFields = new HashMap<>();
        }
        return unrecognizedFields;
    }

    private Map<String, Object> unrecognizedFields;
}
