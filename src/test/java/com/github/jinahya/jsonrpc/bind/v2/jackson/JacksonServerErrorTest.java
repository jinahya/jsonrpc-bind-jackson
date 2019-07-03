package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class JacksonServerErrorTest<T extends JacksonResponse.JacksonError.JacksonServerError>
        extends JacksonErrorTest<T, JsonNode> {

    // -----------------------------------------------------------------------------------------------------------------
    public JacksonServerErrorTest(final Class<? extends T> objectClass) {
        super(objectClass, JsonNode.class);
    }
}
