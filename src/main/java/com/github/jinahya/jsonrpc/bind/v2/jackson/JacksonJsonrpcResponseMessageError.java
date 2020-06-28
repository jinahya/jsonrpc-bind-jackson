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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcResponseMessageError;

import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;

/**
 * A class implements {@link com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IJsonrpcResponseMessageError<JacksonJsonrpcResponseMessageError> {

    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_DATA + "=" + data
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + "=" + unrecognizedProperties
               + "}";
    }

    @JsonProperty
    private BaseJsonNode data;

    private Map<String, Object> unrecognizedProperties;
}
