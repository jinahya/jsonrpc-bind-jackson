package com.github.jinahya.jsonrpc.bind.v2.example.random_org.v2.basic;

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

import com.github.jinahya.jsonrpc.bind.v2.JsonrpcBindTests;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

import static com.github.jinahya.jsonrpc.bind.v2.BeanValidationTests.requireValid;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcBindTests.acceptResourceStream;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class RandomOrgBasicRequestTest {

    @BeforeEach
    void setThreadLocalCaller() {
        JsonrpcBindTests.THREAD_LOCAL_CALLER.set(getClass());
    }

    @Test
    void generateInteger_01_request() throws IOException {
        acceptResourceStream(
                "generateIntegers_01_request.json",
                s -> {
                    final JacksonJsonrpcRequestMessage message;
                    try {
                        message = getObjectMapper().readValue(s, JacksonJsonrpcRequestMessage.class);
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                    log.debug("message: {}", message);
                    requireValid(message);
                    assertThat(message.getIdAsString()).isNotNull().isEqualTo("42");
                    assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(42);
                    assertThat(message.getMethod())
                            .isNotNull()
                            .isEqualTo("generateIntegers");
                    final GenerateIntegersParams params = message.getParamsAsObject(GenerateIntegersParams.class);
                    requireValid(params);
                    assertEquals("6b1e65b9-4186-45c2-8981-b77a9842c4f0", params.getApiKey());
                    assertEquals(6, params.getN());
                    assertEquals(1, params.getMin());
                    assertEquals(6, params.getMax());
                    assertThat(params.getReplacement()).isNotNull().isEqualTo(true);
                    assertThat(message.getUnrecognizedProperties())
                            .isNotNull()
                            .isEmpty();
                }
        );
    }
}
