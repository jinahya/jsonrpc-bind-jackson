package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jinahya.jsonrpc.glue.v2.jackson.IJacksonMessageGlue;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

final class IJacksonJsonrpcObjectHelper {

    private static Field find(final Class<?> clazz, final String name) throws NoSuchFieldException {
        assert clazz != null;
        assert name != null;
        for (final Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        final Class<?> superclass = clazz.getSuperclass();
        if (superclass == null) {
            throw new NoSuchFieldException("no field named as " + name);
        }
        return find(superclass, name);
    }

    private static final Map<Class<?>, Map<String, Field>> FIELDS = synchronizedMap(new WeakHashMap<>());

    private static Field field(final Class<?> clazz, final String name) {
        assert clazz != null;
        assert name != null;
        return FIELDS.computeIfAbsent(clazz, c -> synchronizedMap(new HashMap<>()))
                .computeIfAbsent(name, n -> {
                    try {
                        final Field field = find(clazz, n);
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        return field;
                    } catch (final NoSuchFieldException nsfe) {
                        throw new RuntimeException(nsfe);
                    }
                });
    }

    private static final Map<Class<?>, Map<String, MethodHandle>> GETTERS = synchronizedMap(new WeakHashMap<>());

    private static MethodHandle getter(final Class<?> clazz, final String name) {
        assert clazz != null;
        assert name != null;
        return GETTERS.computeIfAbsent(clazz, c -> synchronizedMap(new HashMap<>()))
                .computeIfAbsent(name, n -> {
                    try {
                        return lookup().unreflectGetter(field(clazz, n));
                    } catch (final IllegalAccessException iae) {
                        throw new RuntimeException(iae);
                    }
                });
    }

    private static final Map<Class<?>, Map<String, MethodHandle>> SETTERS = synchronizedMap(new WeakHashMap<>());

    private static MethodHandle setter(final Class<?> clazz, final String name) {
        assert clazz != null;
        assert name != null;
        return SETTERS.computeIfAbsent(clazz, c -> synchronizedMap(new HashMap<>()))
                .computeIfAbsent(name, n -> {
                    try {
                        return lookup().unreflectSetter(field(clazz, n));
                    } catch (final IllegalAccessException iae) {
                        throw new RuntimeException(iae);
                    }
                });
    }

    public static Object get(final Class<?> clazz, final String name, final Object object) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(name, "name is null");
        requireNonNull(object, "object is null");
        try {
            return getter(clazz, name).invoke(object);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void set(final Class<?> clazz, final String name, final Object object, final Object value) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(name, "name is null");
        requireNonNull(object, "object is null");
        try {
            setter(clazz, name).invoke(object, value);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends IJacksonMessageGlue> T glue(final Class<?> clazz, final Object object) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(object, "object is null");
        final Object value = get(clazz, "glue", object);
        assert value != null;
        return (T) value;
    }

    @SuppressWarnings({"unchecked"})
    public static ObjectMapper mapper(final Class<?> clazz, final Object object) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(object, "object is null");
        final Object value = get(clazz, "mapper", object);
        assert value != null;
        return (ObjectMapper) value;
    }

    private IJacksonJsonrpcObjectHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
