package com.github.jinahya.jsonrpc.bind.v2.jackson;

public abstract class JacksonJsonrpcMessage
        implements IJacksonJsonrpcMessage {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "jsonrpc=" + getJsonrpc()
               + ",id" + getIdAsString()
               + "}";
    }
}
