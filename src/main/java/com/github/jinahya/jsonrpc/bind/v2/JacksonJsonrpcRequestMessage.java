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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.IJsonrpcObjectHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.getObjectMapper;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

/**
 * A class implements {@link com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessage} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE,
                setterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public class JacksonJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJsonrpcRequestMessage<JacksonJsonrpcRequestMessage> {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_PARAMS + "=" + params
               + "," + PROPERTY_NAME_ID + "=" + id
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + "=" + unrecognizedProperties
               + "}";
    }

    // -------------------------------------------------------------------------------------------------------- $.params
    @Override
    public boolean hasParams() {
        return params != null && !params.isNull();
    }

    @Override
    public @AssertTrue boolean isParamsContextuallyValid() {
        return !hasParams() || params instanceof ArrayNode || params instanceof ObjectNode;
    }

    @Override
    public <T> List<T> getParamsAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasParams()) {
            return null;
        }
        final ObjectMapper mapper = getObjectMapper();
        final TypeFactory factory = mapper.getTypeFactory();
        if (params.isArray()) {
            try {
                return mapper.convertValue(
                        params, factory.constructCollectionType(List.class, elementClass));
            } catch (final IllegalArgumentException iae) {
                throw new JsonrpcBindException(iae.getCause());
            }
        }
        assert params.isObject();
        try {
            return new ArrayList<>(singletonList(getParamsAsObject(elementClass)));
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae.getCause());
        }
    }

    @Override
    public void setParamsAsArray(final List<?> params) {
        if (params == null) {
            this.params = null;
            return;
        }
        final ObjectMapper mapper = getObjectMapper();
        this.params = (ArrayNode) mapper.valueToTree(params);
    }

    @Override
    public <T> T getParamsAsObject(final Class<T> objectClass) {
        if (!hasParams()) {
            return null;
        }
        requireNonNull(objectClass, "objectClass is null");
        if (params == null) {
            return null;
        }
        final ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.convertValue(params, objectClass);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae.getCause());
        }
    }

    @Override
    public void setParamsAsObject(final Object params) {
        if (params == null) {
            this.params = null;
            return;
        }
        final ObjectMapper objectMapper = getObjectMapper();
        final JsonNode tree;
        try {
            tree = objectMapper.valueToTree(params);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae.getCause());
        }
        assert tree != null;
        if (!(tree instanceof ContainerNode)) {
            throw new JsonrpcBindException("illegal value for params: " + params);
        }
        this.params = (ContainerNode<?>) tree;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    private ContainerNode<?> params;

    @JsonProperty
    private ValueNode id;

    private Map<String, Object> unrecognizedProperties;
}