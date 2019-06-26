package com.github.jinahya.jsonrpc.bind.v2.jackson;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.List;

import static java.util.Arrays.asList;

@Deprecated
final class JacksonUtils {

    // -----------------------------------------------------------------------------------------------------------------
    static boolean isEitherArrayObjectOrNull(final JsonNode node) {
        if (node == null) {
            throw new NullPointerException("Node is null");
        }
        return node instanceof ArrayNode || node instanceof ObjectNode || node instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static boolean isEitherTextNumericOrNull(final ValueNode node) {
        if (node == null) {
            throw new NullPointerException("Node is null");
        }
        return node instanceof TextNode || node instanceof NumericNode || node instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static JsonNode arrayNodeOf(final ObjectMapper mapper, final List<?> elements) {
        if (elements == null) {
            return null;
        }
        final ArrayNode node = mapper.createArrayNode();
        for (final Object element : elements) {
            node.add(mapper.valueToTree(element));
        }
        return node;
    }

    static JsonNode objectNodeOf(final ObjectMapper mapper, final Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            return arrayNodeOf(mapper, asList((Object[]) value));
        }
        return mapper.valueToTree(value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JacksonUtils() {
        super();
    }
}
