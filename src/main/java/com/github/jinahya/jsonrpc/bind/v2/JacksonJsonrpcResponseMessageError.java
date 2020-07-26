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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import jakarta.validation.constraints.AssertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.IJacksonJsonrpcObjectHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;
import static com.github.jinahya.jsonrpc.bind.v2.IJacksonJsonrpcObjectHelper.arrayToList;
import static com.github.jinahya.jsonrpc.bind.v2.IJacksonJsonrpcObjectHelper.listToArray;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcConfiguration.getObjectMapper;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageErrorConstants.PROPERTY_NAME_DATA;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

/**
 * A class implements {@link com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
                setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JacksonJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IJacksonJsonrpcResponseMessageError<JacksonJsonrpcResponseMessageError> {

    @Override
    public String toString() {
        return super.toString() + '{'
               + PROPERTY_NAME_DATA + '=' + data
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + '=' + unrecognizedProperties
               + '}';
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Override
    public boolean hasData() {
        return data != null && !data.isNull();
    }

    @Override
    @AssertTrue
    public boolean isDataContextuallyValid() {
        return super.isDataContextuallyValid();
    }

    @Override
    public <T> List<T> getDataAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasData()) {
            return null;
        }
        if (data.isArray()) {
            return arrayToList((ArrayNode) data, elementClass);
        }
        return new ArrayList<>(singletonList(getDataAsObject(elementClass)));
    }

    @Override
    public void setDataAsArray(final List<?> data) {
        if (data == null) {
            this.data = null;
            return;
        }
        this.data = listToArray(data);
    }

    @Override
    public <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasData()) {
            return null;
        }
        try {
            return getObjectMapper().convertValue(data, objectClass);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    @Override
    public void setDataAsObject(final Object data) {
        if (data == null) {
            this.data = null;
            return;
        }
        try {
            this.data = getObjectMapper().valueToTree(data);
        } catch (final IllegalArgumentException iae) {
            throw new JsonrpcBindException(iae);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    private BaseJsonNode data;

    // -----------------------------------------------------------------------------------------------------------------
    private Map<String, Object> unrecognizedProperties;
}
