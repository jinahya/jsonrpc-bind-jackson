package com.github.jinahya.jsonrpc.bind.v2.example.jsonrpc_org;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcBindTests;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.BeanValidationTests.requireValid;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcBindTests.acceptResourceStream;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcBindTests.applyResourceStream;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcResponseMessage.readValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class JsonrpcOrgResponseTest {

    @BeforeEach
    void setThreadLocalCaller() {
        JsonrpcBindTests.THREAD_LOCAL_CALLER.set(getClass());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void r_e01_positional_parameters_01_response() throws IOException {
        acceptResourceStream(
                "e01_positional_parameters_01_response.json",
                s -> {
                    final JacksonJsonrpcResponseMessage message = readValue(s);
                    requireValid(message);
                    {
                        assertTrue(message.hasResult());
                        assertThat(message.getResultAsArray(BigInteger.class)).isNotNull()
                                .containsExactly(BigInteger.valueOf(19L));
                        assertThat(message.getResultAsArray(int.class)).isNotNull().containsExactly(19);
                        assertThat(message.getResultAsArray(Integer.class)).isNotNull().containsExactly(19);
                        assertThat(message.getResultAsArray(long.class)).isNotNull().containsExactly(19L);
                        assertThat(message.getResultAsArray(Long.class)).isNotNull().containsExactly(19L);
                    }
                    {
                        assertFalse(message.hasError());
                    }
                    {
                        assertTrue(message.hasId());
                        assertEquals("1", message.getIdAsString());
                        assertThat(message.getIdAsNumber()).isNotNull()
                                .isEqualByComparingTo(BigInteger.valueOf(1L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(1L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(1);
                    }
                    {
                        assertTrue(message.getUnrecognizedProperties().isEmpty());
                    }
                }
        );
    }

    @Test
    void w_e01_positional_parameters_01_response() throws IOException {
        final JsonNode expected = applyResourceStream("e01_positional_parameters_01_response.json", s -> {
            try {
                return getObjectMapper().readTree(s);
            } catch (final IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        });
        final JacksonJsonrpcResponseMessage message = new JacksonJsonrpcResponseMessage();
        message.setResultAsObject(19);
        message.setIdAsInteger(1);
        final JsonNode actual = getObjectMapper().valueToTree(message);
        assertEquals(expected, actual);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void e02_named_parameters_01_response() throws IOException {
        acceptResourceStream(
                "e02_named_parameters_01_response.json",
                s -> {
                    final JacksonJsonrpcResponseMessage message = readValue(s);
                    requireValid(message);
                    {
                        assertTrue(message.hasResult());
                        {
                            final List<BigInteger> result = message.getResultAsArray(BigInteger.class);
                            assertThat(result).isNotNull().contains(BigInteger.valueOf(19L));
                        }
                        {
                            final List<Integer> result = message.getResultAsArray(int.class);
                            assertThat(result).isNotNull().contains(19);
                        }
                        {
                            final List<Integer> result = message.getResultAsArray(Integer.class);
                            assertThat(result).isNotNull().contains(19);
                        }
                        {
                            final List<Long> result = message.getResultAsArray(Long.class);
                            assertThat(result).isNotNull().contains(19L);
                        }
                    }
                    {
                        assertFalse(message.hasError());
                    }
                    {
                        assertTrue(message.hasId());
                        assertEquals("3", message.getIdAsString());
                        assertThat(message.getIdAsNumber()).isNotNull()
                                .isEqualByComparingTo(BigInteger.valueOf(3L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(3L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(3);
                    }
                    {
                        log.debug("unrecognized properties: {}", message.getUnrecognizedProperties());
                    }
                }
        );
    }

    @Test
    void e02_named_parameters_02_response() throws IOException {
        acceptResourceStream(
                "e02_named_parameters_02_response.json",
                s -> {
                    final JacksonJsonrpcResponseMessage message = readValue(s);
                    requireValid(message);
                    {
                        assertTrue(message.hasResult());
                        {
                            final List<BigInteger> result = message.getResultAsArray(BigInteger.class);
                            assertThat(result).isNotNull().contains(BigInteger.valueOf(19L));
                        }
                        {
                            final List<Integer> result = message.getResultAsArray(int.class);
                            assertThat(result).isNotNull().contains(19);
                        }
                        {
                            final List<Integer> result = message.getResultAsArray(Integer.class);
                            assertThat(result).isNotNull().contains(19);
                        }
                        {
                            final List<Long> result = message.getResultAsArray(long.class);
                            assertThat(result).isNotNull().contains(19L);
                        }
                        {
                            final List<Long> result = message.getResultAsArray(Long.class);
                            assertThat(result).isNotNull().contains(19L);
                        }
                    }
                    {
                        assertFalse(message.hasError());
                    }
                    {
                        assertTrue(message.hasId());
                        assertEquals("4", message.getIdAsString());
                        assertThat(message.getIdAsNumber()).isNotNull()
                                .isEqualByComparingTo(BigInteger.valueOf(4L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(4L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(4);
                    }
                    {
                        log.debug("unrecognized properties: {}", message.getUnrecognizedProperties());
                    }
                }
        );
    }

    @Test
    void e04_non_existent_method_response() throws IOException {
        acceptResourceStream(
                "e04_non_existent_method_response.json",
                s -> {
                    final JacksonJsonrpcResponseMessage message = readValue(s);
                    requireValid(message);
                    {
                        assertFalse(message.hasResult());
                    }
                    {
                        assertTrue(message.hasError());
                        final JsonrpcResponseMessageError error = message.getErrorAsDefaultType();
                        requireValid(error);
                        assertEquals(-32601, error.getCode());
                        assertEquals("Method not found", error.getMessage());
                        assertFalse(error.hasData());
                    }
                    {
                        assertTrue(message.hasId());
                        assertEquals("1", message.getIdAsString());
                        assertThat(message.getIdAsNumber()).isNotNull()
                                .isEqualByComparingTo(BigInteger.valueOf(1L));
                        assertThat(message.getIdAsLong()).isNotNull().isEqualTo(1L);
                        assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(1);
                    }
                }
        );
    }
}
