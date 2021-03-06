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
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
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
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageConstants.PROPERTY_NAME_ERROR;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageConstants.PROPERTY_NAME_RESULT;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * A class implements {@link com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE,
                setterVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public class JacksonJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IJacksonJsonrpcResponseMessage<JacksonJsonrpcResponseMessage> {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + PROPERTY_NAME_ID + '=' + id
               + "," + PROPERTY_NAME_RESULT + '=' + result
               + "," + PROPERTY_NAME_ERROR + '=' + error
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

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    public boolean hasResult() {
        return result != null && !result.isNull();
    }

    @Override
    public boolean isResultContextuallyValid() {
        return super.isResultContextuallyValid();
    }

    @Override
    public <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasResult()) {
            return null;
        }
        if (result.isArray()) {
            return arrayToList((ArrayNode) result, elementClass);
        }
        return new ArrayList<>(singletonList(getResultAsObject(elementClass)));
    }

    @Override
    public void setResultAsArray(final List<?> result) {
        if (result == null) {
            this.result = null;
            return;
        }
        this.result = listToArray(result);
    }

    @Override
    public <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        try {
            return getObjectMapper().convertValue(result, objectClass);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    @Override
    public void setResultAsObject(final Object result) {
        if (result == null) {
            this.result = null;
            return;
        }
        try {
            this.result = getObjectMapper().valueToTree(result);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    // --------------------------------------------------------------------------------------------------------- $.error
    @Override
    public boolean hasError() {
        return error != null && !error.isNull();
    }

    @Override
    public boolean isErrorContextuallyValid() {
        return super.isErrorContextuallyValid();
    }

    @Override
    public JsonrpcResponseMessageError getErrorAs() {
//        requireNonNull(clazz, "clazz is null");
        if (!hasError()) {
            return null;
        }
        try {
            return getObjectMapper().convertValue(error, JacksonJsonrpcResponseMessageError.class);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    @Override
    public void setErrorAs(final JsonrpcResponseMessageError error) {
        if (error == null) {
            this.error = null;
            return;
        }
        try {
            this.error = getObjectMapper().valueToTree(error);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

//    @Override
//    public JsonrpcResponseMessageError getErrorAsDefaultType() {
//        return getErrorAs(JacksonJsonrpcResponseMessageError.class);
//    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    private BaseJsonNode result;

    @JsonProperty
    private ObjectNode error;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    private ValueNode id;

    // -----------------------------------------------------------------------------------------------------------------
    private Map<String, Object> unrecognizedProperties;
}
