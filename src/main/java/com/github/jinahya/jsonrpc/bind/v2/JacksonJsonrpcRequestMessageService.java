package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.spi.JsonrpcRequestMessageService;

import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcMessages.readValue;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcMessages.writeValue;
import static java.util.Objects.requireNonNull;

public class JacksonJsonrpcRequestMessageService
        implements JsonrpcRequestMessageService {

    @Override
    public JsonrpcRequestMessage newInstance() {
        return new JacksonJsonrpcRequestMessage();
    }

    @Override
    public JsonrpcRequestMessage fromJson(final Object source) {
        requireNonNull(source, "source is null");
        return readValue(source, JacksonJsonrpcRequestMessage.class);
    }

    @Override
    public void toJson(final JsonrpcRequestMessage message, final Object target) {
        requireNonNull(message, "message is null");
        requireNonNull(target, "target is null");
        writeValue(target, message);
    }
}
