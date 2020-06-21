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
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcRequestMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;
import static java.util.Objects.requireNonNull;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public class JacksonJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJsonrpcRequestMessage {

    public static <T extends JacksonJsonrpcRequestMessage> T readValue(final Object source, final Object type) {
        requireNonNull(source, "source is null");
        requireNonNull(type, "type is null");
        return IJsonrpcMessageHelper.readValue(source, type);
    }

    public static <T extends JacksonJsonrpcRequestMessage> T readValue(final Object source) {
        requireNonNull(source, "source is null");
        return readValue(source, JacksonJsonrpcRequestMessage.class);
    }

    public static <T extends JacksonJsonrpcRequestMessage> void writeValue(final Object target, final T value) {
        requireNonNull(target, "target is null");
        requireNonNull(value, "value is null");
        IJsonrpcMessageHelper.writeValue(target, value);
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_ID + "=" + id
               + "," + PROPERTY_NAME_PARAMS + "=" + params
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + "=" + unrecognizedProperties
               + "}";
    }

    @JsonProperty
    private ValueNode id;

    @JsonProperty
    private ContainerNode<?> params;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Map<String, Object> unrecognizedProperties;
}
