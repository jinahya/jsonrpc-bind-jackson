package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.spi.JsonrpcResponseMessageService;

import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcMessages.readValue;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcMessages.writeValue;
import static java.util.Objects.requireNonNull;

public class JacksonJsonrpcResponseMessageService implements JsonrpcResponseMessageService {

    @Override
    public JsonrpcResponseMessage newInstance() {
        return new JacksonJsonrpcResponseMessage();
    }

    @Override
    public JsonrpcResponseMessage fromJson(final Object source) {
        requireNonNull(source, "source is null");
        return readValue(source, JacksonJsonrpcResponseMessage.class);
    }

    @Override
    public void toJson(final JsonrpcResponseMessage message, final Object target) {
        requireNonNull(message, "message is null");
        requireNonNull(target, "target is null");
        writeValue(target, message);
    }
}
