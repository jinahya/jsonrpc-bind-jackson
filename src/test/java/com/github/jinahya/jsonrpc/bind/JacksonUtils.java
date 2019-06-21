package com.github.jinahya.jsonrpc.bind;

/*-
 * #%L
 * jsonrpc-bind
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Slf4j
public final class JacksonUtils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // fully thread-safe!

    public static final JsonFactory JSON_FACTORY = new JsonFactory();

    public static <R> R applyObjectMapper(final Function<? super ObjectMapper, ? extends R> function) {
        return function.apply(OBJECT_MAPPER);
    }

    public static <U, R> R applyObjectMapper(final Supplier<? extends U> supplier,
                                             final BiFunction<? super ObjectMapper, ? super U, ? extends R> function) {
        return applyObjectMapper(v -> function.apply(v, supplier.get()));
    }

    public static void acceptObjectMapper(final Consumer<? super ObjectMapper> consumer) {
        applyObjectMapper(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static <U> void acceptObjectMapper(final Supplier<? extends U> supplier,
                                              final BiConsumer<? super ObjectMapper, ? super U> consumer) {
        acceptObjectMapper(v -> consumer.accept(v, supplier.get()));
    }

    public static <T> void readTreeFromResource(final String resourceName, final Class<? extends T> valueClass,
                                                final Consumer<? super JsonNode> treeConsumer)
            throws IOException {
        try (InputStream resourceStream = valueClass.getResourceAsStream(resourceName)) {
            assertNotNull(resourceStream, "null resource stream for " + resourceName);
            final JsonNode tree = OBJECT_MAPPER.readTree(resourceStream);
            treeConsumer.accept(tree);
        }
    }

    public static <T> T withResource(final String resourceName, final Class<? extends T> valueClass,
                                     final BiConsumer<? super T, ? super String> valueConsumer)
            throws IOException {
        try (InputStream resourceStream = valueClass.getResourceAsStream(resourceName)) {
            assertNotNull(resourceStream, "null resource stream for " + resourceName);
            final T value = requireValid(OBJECT_MAPPER.readValue(resourceStream, valueClass));
            final String string = OBJECT_MAPPER.writeValueAsString(value);
            log.debug("jackson: {}", value);
            log.debug("jackson: {}", string);
            valueConsumer.accept(value, string);
            return value;
        }
    }

    public static <T> T withResource(final String resourceName, final Class<? extends T> valueClass,
                                     final Consumer<? super String> stringConsumer)
            throws IOException {
        return withResource(resourceName, valueClass, (v, s) -> stringConsumer.accept(s));
    }

    public static <T> T readValue(final String resourceName, final Class<? extends T> valueClass)
            throws IOException {
        try (InputStream resourceStream = valueClass.getResourceAsStream(resourceName)) {
            assertNotNull(resourceStream, "null resource stream for " + resourceName);
            final T value = requireValid(OBJECT_MAPPER.readValue(resourceStream, valueClass));
            final String string = OBJECT_MAPPER.writeValueAsString(value);
            log.debug("jackson: {}", value);
            log.debug("jackson: {}", string);
            return value;
        }
    }

    private JacksonUtils() {
        super();
    }
}
