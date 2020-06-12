package com.github.jinahya.jsonrpc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class JsonrpcTests {

    /**
     * Applies a stream of specified resource to specified function and returns the result.
     *
     * @param name     the resource name to open.
     * @param function the function to be applied with the resource stream.
     * @param <R>      result type parameter.
     * @return the result of the function
     * @throws IOException if an I/O error occurs.
     */
    public static <R> R applyResourceStream(final String name,
                                            final Function<? super InputStream, ? extends R> function)
            throws IOException {
        requireNonNull(name, "name is null");
        requireNonNull(function, "function is null");
        try (InputStream resource = currentThread().getContextClassLoader().getResourceAsStream(name)) {
            assertNotNull(resource, "null resource stream loaded from '" + name + "'");
            return function.apply(resource);
        }
    }

    private JsonrpcTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
