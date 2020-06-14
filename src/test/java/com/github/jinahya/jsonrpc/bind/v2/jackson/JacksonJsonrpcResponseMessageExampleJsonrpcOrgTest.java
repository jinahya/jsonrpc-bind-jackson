package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;

import static com.github.jinahya.jsonrpc.BeanValidations.requireValid;
import static com.github.jinahya.jsonrpc.JacksonTests.acceptObjectMapper;
import static com.github.jinahya.jsonrpc.JsonrpcTests.acceptResourceStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JacksonJsonrpcResponseMessageExampleJsonrpcOrgTest {

    static class NamedParams {

        public int subtrahend;

        public int minuend;
    }

    @Test
    void example_jsonrpc_org_named_parameters_01_response() throws IOException {
        acceptResourceStream(
                "examples/jsonrpc.org/named_parameters_01_response.json",
                s -> {
                    acceptObjectMapper(m -> {
                        final JacksonJsonrpcResponseMessage message;
                        try {
                            message = m.readValue(s, JacksonJsonrpcResponseMessage.class);
                        } catch (final IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                        log.debug("message: {}", message);
                        requireValid(message);
                        {
                            assertTrue(message.hasResult());
                            assertThrows(JsonrpcBindException.class, () -> message.getResultAsString());
                            assertEquals("19", message.getResultAsString(true));
                        }
                        {
                            assertTrue(message.hasId());
                            assertThat(message.getIdAsString(true)).isNotNull().isEqualTo("3");
                            assertThat(message.getIdAsNumber(true)).isNotNull()
                                    .isEqualByComparingTo(BigInteger.valueOf(3L));
                            assertThat(message.getIdAsLong(true)).isNotNull().isEqualTo(3L);
                            assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(3);
                        }
                    });
                }
        );
    }
}