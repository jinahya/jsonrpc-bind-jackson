package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.JacksonUtils.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonUtils.readValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamedParametersTest {

    @Test
    void named_parameters_01_request() throws IOException {
        {
            final NamedParametersClientRequest clientRequest
                    = readValue("named_parameters_01_request.json", NamedParametersClientRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(23, params.getSubtrahend());
                assertEquals(42, params.getMinuend());
                assertEquals(3, (int) clientRequest.getId());
            }
            final NamedParametersServerRequest serverRequest = OBJECT_MAPPER.readValue(
                    OBJECT_MAPPER.writeValueAsString(clientRequest), NamedParametersServerRequest.class);
            final JsonNode params = serverRequest.getParams();
            assertNotNull(params);
            assertTrue(params.isObject());
            assertEquals(23, params.get("subtrahend").asInt());
            assertEquals(42, params.get("minuend").asInt());
            final NumericNode id = serverRequest.getId();
            assertNotNull(id);
            assertTrue(id.isIntegralNumber());
            assertEquals(3, id.asInt());
        }
        {
            final NamedParametersServerRequest clientRequest
                    = readValue("named_parameters_01_request.json", NamedParametersServerRequest.class);
            final NamedParametersClientRequest serverRequest = OBJECT_MAPPER.readValue(
                    OBJECT_MAPPER.writeValueAsString(clientRequest), NamedParametersClientRequest.class);
        }
    }
}
