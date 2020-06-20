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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcResponseMessage;

import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class JacksonJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IJsonrpcResponseMessage {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "id=" + id
               + ",result=" + result
               + ",error=" + error
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + "=" + unrecognizedProperties
               + "}";
    }

    protected ValueNode getId() {
        return id;
    }

    protected void setId(final ValueNode id) {
        this.id = id;
    }

    protected BaseJsonNode getResult() {
        return result;
    }

    protected void setResult(final BaseJsonNode result) {
        this.result = result;
    }

    protected ObjectNode getError() {
        return error;
    }

    protected void setError(final ObjectNode error) {
        this.error = error;
    }

    private ValueNode id;

    private BaseJsonNode result;

    private ObjectNode error;

    private Map<String, Object> unrecognizedProperties;
}
