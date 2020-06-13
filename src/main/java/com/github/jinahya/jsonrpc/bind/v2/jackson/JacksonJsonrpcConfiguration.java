package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Objects.requireNonNull;

/**
 * A configuration class for JSONRPC with Jackson.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class JacksonJsonrpcConfiguration {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static synchronized ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static synchronized void setObjectMapper(final ObjectMapper objectMapper) {
        JacksonJsonrpcConfiguration.objectMapper = requireNonNull(objectMapper, "objectMapper is null");
    }

    private JacksonJsonrpcConfiguration() {
        throw new AssertionError("instantiation is not allowed");
    }
}
