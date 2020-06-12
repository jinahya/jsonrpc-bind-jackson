package com.github.jinahya.jsonrpc.bind.v2.jackson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import static com.github.jinahya.jsonrpc.BeanValidations.requireValid;
import static com.github.jinahya.jsonrpc.JacksonTests.applyObjectMapper;
import static com.github.jinahya.jsonrpc.JsonrpcTests.applyResourceStream;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@Slf4j
class JacksonJsonrpcRequestMessageTest {

    @Test
    void jsonrpc_org_positional_parameters_01_request() throws IOException {
        applyResourceStream(
                "examples/jsonrpc.org/positional_parameters_01_request.json",
                s -> {
                    return applyObjectMapper(m -> {
                        try {
                            final JacksonJsonrpcRequestMessage message
                                    = m.readValue(s, JacksonJsonrpcRequestMessage.class);
                            log.debug("message: {}", message);
                            requireValid(message);
                            assertEquals("subtract", message.getMethod());
                            final List<Integer> params = message.getParamsAsList(Integer.class);
                            assertIterableEquals(asList(42, 23), params);
                            assertEquals(1, message.getIdAsInteger());
                        } catch (final IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                        return null;
                    });
                }
        );
    }
}