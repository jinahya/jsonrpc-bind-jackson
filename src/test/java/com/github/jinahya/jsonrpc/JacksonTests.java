package com.github.jinahya.jsonrpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.jinahya.jsonrpc.BeanValidations.requireValid;
import static com.github.jinahya.jsonrpc.JsonrpcTests.applyResourceStream;
import static java.util.Objects.requireNonNull;

@Slf4j
public final class JacksonTests {

    /**
     * A shared instance of object mapper.
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); // fully thread-safe!

    static {
//        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <R> R applyObjectMapper(final Function<? super ObjectMapper, ? extends R> function) {
        return requireNonNull(function, "function is null").apply(OBJECT_MAPPER);
    }

    public static void acceptObjectMapper(final Consumer<? super ObjectMapper> consumer) {
        requireNonNull(consumer, "consumer is null");
        applyObjectMapper(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static JsonNode readTreeFromResource(final String resourceName) throws IOException {
        return applyResourceStream(
                resourceName,
                s -> applyObjectMapper(m -> {
                    try {
                        return m.readTree(s);
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
        );
    }

    public static <T> T readValueFromResource(final String resourceName, final Class<? extends T> valueClass)
            throws IOException {
        return applyResourceStream(
                resourceName,
                s -> applyObjectMapper(m -> {
                    try {
                        return requireValid(m.readValue(s, valueClass));
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
        );
    }

    public static <T> T readValueFromResource(final String resourceName, final JavaType javaType)
            throws IOException {
        return applyResourceStream(
                resourceName,
                s -> applyObjectMapper(m -> {
                    try {
                        final T value = requireValid(OBJECT_MAPPER.readValue(s, javaType));
                        final String string = OBJECT_MAPPER.writeValueAsString(value);
                        log.debug("jackson: {}", value);
                        log.debug("jackson: {}", string);
                        return value;
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
        );
    }

    public static <T> T readValueFromResource(final String resourceName, final TypeReference<T> typeReference)
            throws IOException {
        return applyResourceStream(
                resourceName,
                s -> applyObjectMapper(m -> {
                    try {
                        final T value = requireValid(m.readValue(s, typeReference));
                        final String string = m.writeValueAsString(value);
                        log.debug("jackson: {}", value);
                        log.debug("jackson: {}", string);
                        return value;
                    } catch (final IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                })
        );
    }

    private JacksonTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
