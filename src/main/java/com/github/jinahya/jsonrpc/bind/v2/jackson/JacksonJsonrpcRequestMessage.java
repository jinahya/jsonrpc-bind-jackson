package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jinahya.jsonrpc.glue.v2.jackson.JacksonRequestMessageGlue;

import static java.util.Objects.requireNonNull;

public class JacksonJsonrpcRequestMessage
        extends JacksonJsonrpcMessage
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
               + "method=" + getMethod()
               + ",params=" + getParamsAsObject(Object.class)
               + "}";
    }

    private final ObjectMapper mapper;

    private final JacksonRequestMessageGlue glue = new JacksonRequestMessageGlue();
}
