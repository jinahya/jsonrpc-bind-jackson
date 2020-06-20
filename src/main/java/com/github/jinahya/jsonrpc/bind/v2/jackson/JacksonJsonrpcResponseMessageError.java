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
import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcResponseMessageError;

import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;

public class JacksonJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IJsonrpcResponseMessageError {

    @Override
    public String toString() {
        return super.toString() + "{"
               + "data=" + data
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + "=" + unrecognizedProperties
               + "}";
    }

    protected BaseJsonNode getData() {
        return data;
    }

    protected void setData(final BaseJsonNode data) {
        this.data = data;
    }

    private BaseJsonNode data;

    private Map<String, Object> unrecognizedProperties;
}
