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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import jakarta.validation.constraints.AssertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.IJacksonJsonrpcObjectHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;
import static com.github.jinahya.jsonrpc.bind.v2.IJacksonJsonrpcObjectHelper.arrayToList;
import static com.github.jinahya.jsonrpc.bind.v2.IJacksonJsonrpcObjectHelper.listToArray;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.getObjectMapper;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcMessageConstants.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessageConstants.PROPERTY_NAME_PARAMS;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

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
        implements IJacksonJsonrpcRequestMessage<JacksonJsonrpcRequestMessage> {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + PROPERTY_NAME_PARAMS + '=' + params
               + "," + PROPERTY_NAME_ID + '=' + id
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + '=' + unrecognizedProperties
               + '}';
    }

    // -------------------------------------------------------------------------------------------------------------- id
    @Override
    public boolean hasId() {
        return id != null && !id.isNull();
    }

    @Override
    @AssertTrue
    public boolean isIdContextuallyValid() {
        if (!hasId()) {
            return true;
        }
        return id.isTextual() || id.isIntegralNumber();
    }

    @Override
    public String getIdAsString() {
        if (!hasId()) {
            return null;
        }
        return id.asText();
    }

    @Override
    public void setIdAsString(final String id) {
        this.id = ofNullable(id).map(TextNode::new).orElse(null);
    }

    @Override
    public BigInteger getIdAsNumber() {
        if (!hasId()) {
            return null;
        }
        if (id.isNumber()) {
            return id.bigIntegerValue(); // BigInteger.ZERO <- !isNumber()
        }
        try {
            return new BigInteger(getIdAsString());
        } catch (final NumberFormatException nfe) {
            // suppressed
        }
        throw new JsonrpcBindException("unable to bind id as a number");
    }

    @Override
    public void setIdAsNumber(final BigInteger id) {
        this.id = ofNullable(id).map(BigIntegerNode::valueOf).orElse(null);
    }

    @Override
    public Long getIdAsLong() {
        if (!hasId()) {
            return null;
        }
        if (id.canConvertToLong()) {
            return id.longValue();
        }
        return super.getIdAsLong();
    }

    @Override
    public void setIdAsLong(final Long id) {
        this.id = ofNullable(id).map(LongNode::new).orElse(null);
    }

    @Override
    public Integer getIdAsInteger() {
        if (!hasId()) {
            return null;
        }
        if (id.canConvertToInt()) {
            return id.intValue();
        }
        return super.getIdAsInteger();
    }

    @Override
    public void setIdAsInteger(final Integer id) {
        this.id = ofNullable(id).map(IntNode::new).orElse(null);
    }

    // ---------------------------------------------------------------------------------------------------------- params
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
        if (params.isArray()) {
            return arrayToList((ArrayNode) params, elementClass);
        }
        return new ArrayList<>(singletonList(getParamsAsObject(elementClass)));
    }

    @Override
    public void setParamsAsArray(final List<?> params) {
        if (params == null) {
            this.params = null;
            return;
        }
        this.params = listToArray(params);
    }

    @Override
    public <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasParams()) {
            return null;
        }
        try {
            return getObjectMapper().convertValue(params, objectClass);
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
        try {
            this.params = getObjectMapper().valueToTree(params);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae.getCause());
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    private ContainerNode<?> params;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    private ValueNode id;

    // -----------------------------------------------------------------------------------------------------------------
    private Map<String, Object> unrecognizedProperties;
}
