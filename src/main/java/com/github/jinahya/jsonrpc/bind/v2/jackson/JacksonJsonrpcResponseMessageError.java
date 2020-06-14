package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.github.jinahya.jsonrpc.bind.v2b.AbstractJsonrpcResponseMessageError;
import jdk.nashorn.internal.ir.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class JacksonJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IJacksonJsonrpcResponseMessageError {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "data=" + data
               + ",unrecognizedFields=" + unrecognizedFields
               + "}";
    }

    protected BaseJsonNode getData() {
        return data;
    }

    protected void setData(final BaseJsonNode data) {
        this.data = data;
    }

    private BaseJsonNode data;

    @JsonAnySetter
    protected Object unrecognizedField(final String name, final Object value) {
        return unrecognizedFields().put(name, value);
    }

    public Map<String, Object> unrecognizedFields() {
        if (unrecognizedFields == null) {
            unrecognizedFields = new HashMap<>();
        }
        return unrecognizedFields;
    }

    private Map<String, Object> unrecognizedFields;
}
