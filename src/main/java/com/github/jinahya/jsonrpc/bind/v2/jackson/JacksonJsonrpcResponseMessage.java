package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2b.AbstractJsonrpcResponseMessage;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class JacksonJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IJacksonJsonrpcResponseMessage {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "id=" + id
               + ",result=" + result
               + ",error=" + error
               + "}";
    }

    protected ValueNode getId() {
        return id;
    }

    protected void setId(final ValueNode id) {
        this.id = id;
    }

    protected BaseJsonNode getResult() {
        return result;
    }

    protected void setResult(final BaseJsonNode result) {
        this.result = result;
    }

    protected ObjectNode getError() {
        return error;
    }

    protected void setError(final ObjectNode error) {
        this.error = error;
    }

    @JsonAnySetter
    private Object unrecognizedField(final String name, final Object value) {
        return unrecognizedFields().put(name, value);
    }

    protected Map<String, Object> unrecognizedFields() {
        if (unrecognizedFields == null) {
            unrecognizedFields = new HashMap<>();
        }
        return unrecognizedFields;
    }

    private ValueNode id;

    private BaseJsonNode result;

    private ObjectNode error;

    private Map<String, Object> unrecognizedFields;
}
