package com.github.jinahya.jsonrpc.bind.v2.jackson;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.util.List;

import static com.github.jinahya.jsonrpc.BeanValidations.requireValid;
import static com.github.jinahya.jsonrpc.JacksonTests.acceptObjectMapper;
import static com.github.jinahya.jsonrpc.JsonrpcTests.acceptResourceStream;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JacksonJsonrpcRequestMessageExampleJsonrpcOrgTest {

    static class NamedParams {

        public int subtrahend;

        public int minuend;
    }

    @Test
    void example_jsonrpc_org_named_parameters_01_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/named_parameters_01_request.json",
                s -> {
                    acceptObjectMapper(m -> {
                        final JacksonJsonrpcRequestMessage message;
                        try {
                            message = m.readValue(s, JacksonJsonrpcRequestMessage.class);
                        } catch (final IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                        log.debug("message: {}", message);
                        requireValid(message);
                        assertEquals("subtract", message.getMethod());
                        final NamedParams namedParams = message.getParamsAsObject(NamedParams.class);
                        assertEquals(23, namedParams.subtrahend);
                        assertEquals(42, namedParams.minuend);
                        assertTrue(message.hasId());
                        assertThat(message.getIdAsString()).isNotNull().isEqualTo("3");
                        assertThat(message.getIdAsNumber()).isNotNull().isEqualByComparingTo(BigInteger.valueOf(3L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(3L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(3);
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_named_parameters_02_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/named_parameters_02_request.json",
                s -> {
                    acceptObjectMapper(m -> {
                        final JacksonJsonrpcRequestMessage message;
                        try {
                            message = m.readValue(s, JacksonJsonrpcRequestMessage.class);
                        } catch (final IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                        log.debug("message: {}", message);
                        requireValid(message);
                        assertEquals("subtract", message.getMethod());
                        final NamedParams namedParams = message.getParamsAsObject(NamedParams.class);
                        assertEquals(42, namedParams.minuend);
                        assertEquals(23, namedParams.subtrahend);
                        assertTrue(message.hasId());
                        assertThat(message.getIdAsString()).isNotNull().isEqualTo("4");
                        assertThat(message.getIdAsNumber()).isNotNull().isEqualByComparingTo(BigInteger.valueOf(4L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(4L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(4);
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_positional_parameters_01_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/positional_parameters_01_request.json",
                s -> {
                    acceptObjectMapper(m -> {
                        final JacksonJsonrpcRequestMessage message;
                        try {
                            message = m.readValue(s, JacksonJsonrpcRequestMessage.class);
                        } catch (final IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                        log.debug("message: {}", message);
                        requireValid(message);
                        assertEquals("subtract", message.getMethod());
                        final List<Integer> params = message.getParamsAsList(Integer.class);
                        assertIterableEquals(asList(42, 23), params);
                        assertTrue(message.hasId());
                        assertThat(message.getIdAsString()).isNotNull().isEqualTo("1");
                        assertThat(message.getIdAsNumber()).isNotNull().isEqualByComparingTo(BigInteger.valueOf(1L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(1L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(1);
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_positional_parameters_02_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/positional_parameters_02_request.json",
                s -> {
                    acceptObjectMapper(m -> {
                        final JacksonJsonrpcRequestMessage message;
                        try {
                            message = m.readValue(s, JacksonJsonrpcRequestMessage.class);
                        } catch (final IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                        log.debug("message: {}", message);
                        requireValid(message);
                        assertEquals("subtract", message.getMethod());
                        final List<Integer> params = message.getParamsAsList(Integer.class);
                        assertIterableEquals(asList(23, 42), params);
                        assertTrue(message.hasId());
                        assertThat(message.getIdAsString()).isNotNull().isEqualTo("2");
                        assertThat(message.getIdAsNumber()).isNotNull().isEqualByComparingTo(BigInteger.valueOf(2L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(2L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(2);
                    });
                }
        );
    }
}