package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.util.List;

import static com.github.jinahya.jsonrpc.BeanValidationTests.requireValid;
import static com.github.jinahya.jsonrpc.JacksonTests.acceptObjectMapper;
import static com.github.jinahya.jsonrpc.JsonrpcTests.acceptResourceStream;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JacksonJsonrpcRequestMessageExampleJsonrpcOrgTest {

    static class NamedParams {

        public int subtrahend;

        public int minuend;
    }

    @Test
    void example_jsonrpc_org_e01_positional_parameters_01_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/e01_positional_parameters_01_request.json",
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
                        {
                            assertEquals("subtract", message.getMethod());
                        }
                        {
                            assertTrue(message.hasParams());
                            final List<Integer> params = message.getParamsAsArray(Integer.class);
                            assertIterableEquals(asList(42, 23), params);
                            {
                                final Integer[] array = message.getParamsAsObject(Integer[].class);
                                assertThat(array).isNotNull().containsSequence(42, 23);
                            }
                            {
                                final int[] array = message.getParamsAsObject(int[].class);
                                assertThat(array).isNotNull().containsSequence(42, 23);
                            }
                        }
                        {
                            assertTrue(message.hasId());
                            assertEquals("1", message.getIdAsString());
                            assertThat(message.getIdAsNumber()).isNotNull()
                                    .isEqualByComparingTo(BigInteger.valueOf(1L));
                            assertThat(message.getIdAsLong()).isNotNull().isEqualTo(1L);
                            assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(1);
                        }
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_e01_positional_parameters_02_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/e01_positional_parameters_02_request.json",
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
                        {
                            assertEquals("subtract", message.getMethod());
                        }
                        {
                            assertTrue(message.hasParams());
                            final List<Integer> params = message.getParamsAsArray(Integer.class);
                            assertIterableEquals(asList(23, 42), params);
                            {
                                final Integer[] array = message.getParamsAsObject(Integer[].class);
                                assertThat(array).isNotNull().containsSequence(23, 42);
                            }
                            {
                                final int[] array = message.getParamsAsObject(int[].class);
                                assertThat(array).isNotNull().containsSequence(23, 42);
                            }
                        }
                        {
                            assertTrue(message.hasId());
                            assertEquals("2", message.getIdAsString());
                            assertThat(message.getIdAsNumber()).isNotNull()
                                    .isEqualByComparingTo(BigInteger.valueOf(2L));
                            assertThat(message.getIdAsLong()).isNotNull().isEqualTo(2L);
                            assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(2);
                        }
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_e02_named_parameters_01_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/e02_named_parameters_01_request.json",
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
                        {
                            assertEquals("subtract", message.getMethod());
                        }
                        {
                            assertTrue(message.hasParams());
                            final NamedParams params = message.getParamsAsObject(NamedParams.class);
                            assertEquals(23, params.subtrahend);
                            assertEquals(42, params.minuend);
                            {
                                final List<NamedParams> array = message.getParamsAsArray(NamedParams.class);
                                assertThat(array)
                                        .isNotNull()
                                        .hasSize(1)
                                        .allSatisfy(e -> {
                                            assertEquals(23, e.subtrahend);
                                            assertEquals(42, e.minuend);
                                        });
                            }
                        }
                        {
                            assertTrue(message.hasId());
                            assertEquals("3", message.getIdAsString());
                            assertThat(message.getIdAsNumber()).isNotNull()
                                    .isEqualByComparingTo(BigInteger.valueOf(3L));
                            assertThat(message.getIdAsLong()).isNotNull().isEqualTo(3L);
                            assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(3);
                        }
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_e02_named_parameters_02_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/e02_named_parameters_02_request.json",
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
                        {
                            assertEquals("subtract", message.getMethod());
                        }
                        {
                            assertTrue(message.hasParams());
                            final NamedParams params = message.getParamsAsObject(NamedParams.class);
                            assertEquals(42, params.minuend);
                            assertEquals(23, params.subtrahend);
                            {
                                final List<NamedParams> array = message.getParamsAsArray(NamedParams.class);
                                assertThat(array)
                                        .isNotNull()
                                        .hasSize(1)
                                        .allSatisfy(e -> {
                                            assertEquals(42, e.minuend);
                                            assertEquals(23, e.subtrahend);
                                        });
                            }
                        }
                        {
                            assertTrue(message.hasId());
                            assertEquals("4", message.getIdAsString());
                            assertThat(message.getIdAsNumber()).isNotNull()
                                    .isEqualByComparingTo(BigInteger.valueOf(4L));
                            assertThat(message.getIdAsLong()).isNotNull().isEqualTo(4L);
                            assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(4);
                        }
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_e03_notification_01_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/e03_notification_01_request.json",
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
                        {
                            assertEquals("update", message.getMethod());
                        }
                        {
                            assertTrue(message.hasParams());
                            final List<Integer> params = message.getParamsAsArray(int.class);
                            assertThat(params).isNotNull().containsExactly(1, 2, 3, 4, 5);
                        }
                        {
                            assertTrue(message.hasParams());
                            final List<Integer> params = message.getParamsAsArray(Integer.class);
                            assertThat(params).isNotNull().containsExactly(1, 2, 3, 4, 5);
                        }
                        {
                            assertThrows(JsonrpcBindException.class, () -> message.getParamsAsObject(int.class));
                            final int[] params = message.getParamsAsObject(int[].class);
                            assertThat(params).isNotNull().containsSequence(1, 2, 3, 4, 5);
                        }
                        {
                            assertThrows(JsonrpcBindException.class, () -> message.getParamsAsObject(Integer.class));
                            final Integer[] array = message.getParamsAsObject(Integer[].class);
                            assertThat(array).isNotNull().containsExactly(1, 2, 3, 4, 5);
                        }
                        {
                            assertFalse(message.hasId());
                        }
                    });
                }
        );
    }

    @Test
    void example_jsonrpc_org_e03_notification_02_request() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/e03_notification_02_request.json",
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
                        {
                            assertEquals("foobar", message.getMethod());
                        }
                        {
                            assertFalse(message.hasParams());
                        }
                    });
                }
        );
    }
}