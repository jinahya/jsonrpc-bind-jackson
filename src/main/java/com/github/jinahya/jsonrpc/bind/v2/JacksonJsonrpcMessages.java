package com.github.jinahya.jsonrpc.bind.v2;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.WeakHashMap;

import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.lang.invoke.MethodHandles.publicLookup;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

/**
 * A utility class for messages.
 */
final class JacksonJsonrpcMessages {

    private static final Map<Class<?>, MethodHandle> READ_VALUE_HANDLES = synchronizedMap(new WeakHashMap<>());

    private static MethodHandle readValueHandle(final Class<?> clazz) {
        assert clazz != null;
        return READ_VALUE_HANDLES.computeIfAbsent(clazz, k -> {
            try {
                for (Class<?> c = k; c != null; c = c.getSuperclass()) {
                    try {
                        return publicLookup().findVirtual(
                                ObjectMapper.class, "readValue", methodType(Object.class, c, Class.class));
                    } catch (final NoSuchMethodException nsme) {
                        // suppressed
                    }
                }
                throw new NoSuchMethodException("no readValue method for " + k);
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        });
    }

    static <T extends JsonrpcMessage> T readValue(final Object source, final Class<T> clazz) {
        requireNonNull(source, "source is null");
        requireNonNull(clazz, "clazz is null");
        try {
            return clazz.cast(readValueHandle(source.getClass()).invoke(getObjectMapper(), source, clazz));
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static final Map<Class<?>, MethodHandle> WRITE_VALUE_HANDLES = synchronizedMap(new WeakHashMap<>());

    private static MethodHandle writeValueHandle(final Class<?> clazz) {
        assert clazz != null;
        return WRITE_VALUE_HANDLES.computeIfAbsent(clazz, k -> {
            try {
                for (Class<?> c = k; c != null; c = c.getSuperclass()) {
                    try {
                        return publicLookup().findVirtual(
                                ObjectMapper.class, "writeValue", methodType(Void.class, c, Class.class));
                    } catch (final NoSuchMethodException nsme) {
                        // suppressed
                    }
                }
                throw new NoSuchMethodException("no writeValue method for " + k);
            } catch (final ReflectiveOperationException roe) {
                throw new RuntimeException(roe);
            }
        });
    }

    static <T extends JsonrpcMessage> void writeValue(final Object target, final T value) {
        requireNonNull(target, "target is null");
        requireNonNull(value, "value is null");
        try {
            writeValueHandle(target.getClass()).invoke(getObjectMapper(), target, value);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private JacksonJsonrpcMessages() {
        throw new AssertionError("instantiation is not allowed");
    }
}
