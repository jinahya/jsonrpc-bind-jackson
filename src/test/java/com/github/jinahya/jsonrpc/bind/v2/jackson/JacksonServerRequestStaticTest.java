package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JacksonServerRequestStaticTest {

    // -----------------------------------------------------------------------------------------------------------------
    private static class ExtendedJacksonServerRequest extends JacksonServerRequest {

    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void testOfWithNonObjectNode() {
        {
            final ObjectNode objectNode = OBJECT_MAPPER.createObjectNode();
            assertDoesNotThrow(() -> JacksonServerRequest.of(objectNode));
        }
        final ArrayNode arrayNode = OBJECT_MAPPER.createArrayNode();
        assertThrows(IllegalArgumentException.class, () -> JacksonServerRequest.of(arrayNode));
    }
}
