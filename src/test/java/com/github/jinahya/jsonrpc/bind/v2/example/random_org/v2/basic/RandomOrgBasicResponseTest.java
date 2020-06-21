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

import com.github.jinahya.jsonrpc.JsonrpcTests;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;

import static com.github.jinahya.jsonrpc.BeanValidationTests.requireValid;
import static com.github.jinahya.jsonrpc.JsonrpcTests.acceptResourceStream;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonJsonrpcConfiguration.getObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class RandomOrgBasicResponseTest {

    @BeforeEach
    void setThreadLocalCaller() {
        JsonrpcTests.THREAD_LOCAL_CALLER.set(getClass());
    }

    @Test
    void generateInteger_01_response() throws IOException {
        acceptResourceStream(
                "generateIntegers_01_response.json",
                s -> {
                    final JacksonJsonrpcResponseMessage message;
                    try {
                        message = getObjectMapper().readValue(s, JacksonJsonrpcResponseMessage.class);
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                    log.debug("message: {}", message);
                    requireValid(message);
                    assertThat(message.getIdAsString()).isNotNull().isEqualTo("42");
                    assertThat(message.getIdAsInteger()).isNotNull().isEqualTo(42);
                    final GenerateIntegersResult result = message.getResultAsObject(GenerateIntegersResult.class);
                    assertNotNull(result);
                    requireValid(result);
                    assertThat(result.getRandom())
                            .isNotNull()
                            .satisfies(r -> {
                                assertThat(r.getData())
                                        .isNotNull()
                                        .containsExactly(1, 5, 4, 6, 6, 4);
                                assertThat(r.getCompletionTime())
                                        .isNotNull()
                                        .isEqualTo("2011-10-10 13:19:12Z");
                            });
                    assertEquals(16, result.getBitsUsed());
                    assertEquals(199984, result.getBitsLeft());
                    assertEquals(9999, result.getRequestsLeft());
                    assertEquals(0, result.getAdvisoryDelay());
                    assertThat(message.getUnrecognizedProperties())
                            .isNotNull()
                            .isEmpty();
                }
        );
    }
}
