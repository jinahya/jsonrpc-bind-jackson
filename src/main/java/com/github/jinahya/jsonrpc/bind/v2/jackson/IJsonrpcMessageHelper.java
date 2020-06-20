package com.github.jinahya.jsonrpc.bind.v2.jackson;

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

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.HashMap;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcMessage.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessage.PROPERTY_NAME_PARAMS;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage.PROPERTY_NAME_ERROR;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage.PROPERTY_NAME_RESULT;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError.PROPERTY_NAME_DATA;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.get;
import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.set;

final class IJsonrpcMessageHelper {

    static final String PROPERTY_NAME_UNRECOGNIZED_PROPERTIES = "unrecognizedProperties";

    // -----------------------------------------------------------------------------------------------------------------
    static ValueNode getId(final Class<?> clazz, final Object object) {
        return (ValueNode) get(clazz, PROPERTY_NAME_ID, object);
    }

    static void setId(final Class<?> clazz, final Object object, final ValueNode value) {
        set(clazz, PROPERTY_NAME_ID, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static ContainerNode<?> getRequestParams(final Class<?> clazz, final Object object) {
        return (ContainerNode<?>) get(clazz, PROPERTY_NAME_PARAMS, object);
    }

    static void setRequestParams(final Class<?> clazz, final Object object, final ContainerNode<?> value) {
        set(clazz, PROPERTY_NAME_PARAMS, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static BaseJsonNode getResponseResult(final Class<?> clazz, final Object object) {
        return (BaseJsonNode) get(clazz, PROPERTY_NAME_RESULT, object);
    }

    static void setResponseResult(final Class<?> clazz, final Object object, final BaseJsonNode value) {
        set(clazz, PROPERTY_NAME_RESULT, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static ObjectNode getResponseError(final Class<?> clazz, final Object object) {
        return (ObjectNode) get(clazz, PROPERTY_NAME_ERROR, object);
    }

    static void setResponseError(final Class<?> clazz, final Object object, final ObjectNode value) {
        set(clazz, PROPERTY_NAME_ERROR, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static BaseJsonNode getResponseErrorData(final Class<?> clazz, final Object object) {
        return (BaseJsonNode) get(clazz, PROPERTY_NAME_DATA, object);
    }

    static void setResponseErrorData(final Class<?> clazz, final Object object, final BaseJsonNode value) {
        set(clazz, PROPERTY_NAME_DATA, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
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
    private IJsonrpcMessageHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}