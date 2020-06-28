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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcResponseMessage;

import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcObjectHelper.PROPERTY_NAME_UNRECOGNIZED_PROPERTIES;
import static java.util.Objects.requireNonNull;

/**
 * A class implements {@link com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JacksonJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IJsonrpcResponseMessage<JacksonJsonrpcResponseMessage> {

    public static <T extends JacksonJsonrpcResponseMessage> T readValue(final Object source, final Object type) {
        requireNonNull(source, "source is null");
        requireNonNull(type, "type is null");
        return IJsonrpcObjectHelper.readValue(source, type);
    }

    public static JacksonJsonrpcResponseMessage readValue(final Object source) {
        requireNonNull(source, "source is null");
        return readValue(source, JacksonJsonrpcResponseMessage.class);
    }

    public static void writeValue(final Object target, final JacksonJsonrpcResponseMessage value) {
        requireNonNull(target, "target is null");
        requireNonNull(value, "value is null");
        IJsonrpcObjectHelper.writeValue(target, value);
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_RESULT + "=" + result
               + "," + PROPERTY_NAME_ERROR + "=" + error
               + "," + PROPERTY_NAME_ID + "=" + id
               + "," + PROPERTY_NAME_UNRECOGNIZED_PROPERTIES + "=" + unrecognizedProperties
               + "}";
    }

    @JsonProperty
    private BaseJsonNode result;

    @JsonProperty
    private ObjectNode error;

    @JsonProperty
    private ValueNode id;

    //    @Setter(AccessLevel.NONE)
//    @Getter(AccessLevel.NONE)
    private Map<String, Object> unrecognizedProperties;
}
