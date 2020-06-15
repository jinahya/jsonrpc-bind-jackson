package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcMessage.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessage.PROPERTY_NAME_PARAMS;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage.PROPERTY_NAME_ERROR;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage.PROPERTY_NAME_RESULT;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError.PROPERTY_NAME_DATA;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

class IJacksonJsonrpcObjectHelper {

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

    // -----------------------------------------------------------------------------------------------------------------
    static Object get(final Class<?> clazz, final String name, final Object object) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(name, "name is null");
        requireNonNull(object, "object is null");
        try {
            return getter(clazz, name).invoke(object);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    static void set(final Class<?> clazz, final String name, final Object object, final Object value) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(name, "name is null");
        requireNonNull(object, "object is null");
        try {
            setter(clazz, name).invoke(object, value);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static ValueNode id(final Class<?> clazz, final Object object) {
        return (ValueNode) get(clazz, PROPERTY_NAME_ID, object);
    }

    static void id(final Class<?> clazz, final Object object, final ValueNode value) {
        set(clazz, PROPERTY_NAME_ID, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static ContainerNode<?> requestParams(final Class<?> clazz, final Object object) {
        return (ContainerNode<?>) get(clazz, PROPERTY_NAME_PARAMS, object);
    }

    static void requestParams(final Class<?> clazz, final Object object, final ContainerNode<?> value) {
        set(clazz, PROPERTY_NAME_PARAMS, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static BaseJsonNode responseResult(final Class<?> clazz, final Object object) {
        return (BaseJsonNode) get(clazz, PROPERTY_NAME_RESULT, object);
    }

    static void responseResult(final Class<?> clazz, final Object object, final BaseJsonNode value) {
        set(clazz, PROPERTY_NAME_RESULT, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static ObjectNode responseError(final Class<?> clazz, final Object object) {
        return (ObjectNode) get(clazz, PROPERTY_NAME_ERROR, object);
    }

    static void responseError(final Class<?> clazz, final Object object, final ObjectNode value) {
        set(clazz, PROPERTY_NAME_ERROR, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static BaseJsonNode responseErrorData(final Class<?> clazz, final Object object) {
        return (BaseJsonNode) get(clazz, PROPERTY_NAME_DATA, object);
    }

    static void responseErrorData(final Class<?> clazz, final Object object, final BaseJsonNode value) {
        set(clazz, PROPERTY_NAME_DATA, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    IJacksonJsonrpcObjectHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
