package com.github.jinahya.jsonrpc.bind.v2.jackson;

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

final class JacksonUtils {

    // -----------------------------------------------------------------------------------------------------------------
    static boolean isEitherArrayObjectOrNull(final JsonNode node) {
        if (node == null) {
            throw new NullPointerException("Node is null");
        }
        return node instanceof ArrayNode || node instanceof ObjectNode || node instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------
    static boolean isEitherTextNumberOrNull(final ValueNode node) {
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

    static  JsonNode objectNodeOf(final ObjectMapper mapper, final Object value) {
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
