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

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.SUPPLYING_FALSE;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.SUPPLYING_FALSE_;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.SUPPLYING_TRUE;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.SUPPLYING_TRUE_;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.get;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.set;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.supplyingNull;

final class IJsonrpcObjectHelper {

    // -----------------------------------------------------------------------------------------------------------------
    static <N extends JsonNode, R> R hasOneThenMapOrGet(final Class<?> clazz, final Object object,
                                                        final BiFunction<Class<?>, Object, ? extends N> getter,
                                                        final Function<? super N, ? extends R> function,
                                                        final Supplier<? extends R> supplier) {
        assert getter != null;
        assert function != null;
        assert supplier != null;
        final N node = getter.apply(clazz, object);
        if (node != null && !node.isNull()) {
            return function.apply(node);
        }
        return supplier.get();
    }

    static <N extends JsonNode, R> R hasOneThenMapOrNull(final Class<?> clazz, final Object object,
                                                         final BiFunction<Class<?>, Object, ? extends N> getter,
                                                         final Function<? super N, ? extends R> function) {
        return hasOneThenMapOrGet(clazz, object, getter, function, supplyingNull());
    }

    private static <N extends JsonNode> boolean hasOneThenEvaluateOrGet(
            final Class<?> clazz, final Object object, final BiFunction<Class<?>, Object, ? extends N> getter,
            final Predicate<? super N> predicate, final BooleanSupplier supplier) {
        assert predicate != null;
        assert supplier != null;
        assert supplier == SUPPLYING_TRUE || supplier == SUPPLYING_FALSE;
        return hasOneThenMapOrGet(clazz, object, getter, predicate::test,
                                  supplier == SUPPLYING_TRUE ? SUPPLYING_TRUE_ : SUPPLYING_FALSE_);
    }

    static <N extends JsonNode> boolean hasOneThenEvaluateOrTrue(final Class<?> clazz, final Object object,
                                                                 final BiFunction<Class<?>, Object, ? extends N> getter,
                                                                 final Predicate<? super N> predicate) {
        return hasOneThenEvaluateOrGet(clazz, object, getter, predicate, SUPPLYING_TRUE);
    }

    static <N extends JsonNode> boolean hasOneThenEvaluateOrFalse(
            final Class<?> clazz, final Object object, final BiFunction<Class<?>, Object, ? extends N> getter,
            final Predicate<? super N> predicate) {
        return hasOneThenEvaluateOrGet(clazz, object, getter, predicate, SUPPLYING_FALSE);
    }

    // ------------------------------------------------------------------------------------------ unrecognizedProperties
    static final String PROPERTY_NAME_UNRECOGNIZED_PROPERTIES = "unrecognizedProperties";

    @SuppressWarnings({"unchecked"})
    static Map<String, Object> getUnrecognizedProperties(final Class<?> clazz, final Object object) {
        return (Map<String, Object>) get(clazz, PROPERTY_NAME_UNRECOGNIZED_PROPERTIES, object);
    }

    static void setUnrecognizedProperties(final Class<?> clazz, final Object object, final Map<String, Object> value) {
        set(clazz, PROPERTY_NAME_UNRECOGNIZED_PROPERTIES, object, value);
    }

    static Map<String, Object> unrecognizedProperties(final Class<?> clazz, final Object object) {
        final Map<String, Object> unrecognizedProperties = getUnrecognizedProperties(clazz, object);
        if (unrecognizedProperties == null) {
            setUnrecognizedProperties(clazz, object, new HashMap<>());
            return unrecognizedProperties(clazz, object);
        }
        return unrecognizedProperties;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private IJsonrpcObjectHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
