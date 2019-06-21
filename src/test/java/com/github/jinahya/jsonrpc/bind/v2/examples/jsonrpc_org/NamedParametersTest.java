package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonUtils.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonUtils.readValueFromResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamedParametersTest {

    @Test
    void named_parameters_01_request() throws IOException {
        {
            final NamedParametersClientRequest clientRequest
                    = readValueFromResource("named_parameters_01_request.json", NamedParametersClientRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(23, params.getSubtrahend());
                assertEquals(42, params.getMinuend());
                assertEquals(3, (int) clientRequest.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientRequest);
            final NamedParametersServerRequest serverRequest
                    = OBJECT_MAPPER.readValue(string, NamedParametersServerRequest.class);
            {
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
        }
        {
            final NamedParametersServerRequest serverRequest
                    = readValueFromResource("named_parameters_01_request.json", NamedParametersServerRequest.class);
            {
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
            final String string = OBJECT_MAPPER.writeValueAsString(serverRequest);
            final NamedParametersClientRequest clientRequest
                    = OBJECT_MAPPER.readValue(string, NamedParametersClientRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(23, params.getSubtrahend());
                assertEquals(42, params.getMinuend());
                assertEquals(3, (int) clientRequest.getId());
            }
        }
    }

    @Test
    void named_parameters_02_request() throws IOException {
        {
            final NamedParametersClientRequest clientRequest
                    = readValueFromResource("named_parameters_02_request.json", NamedParametersClientRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(42, params.getMinuend());
                assertEquals(23, params.getSubtrahend());
                assertEquals(4, (int) clientRequest.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientRequest);
            final NamedParametersServerRequest serverRequest
                    = OBJECT_MAPPER.readValue(string, NamedParametersServerRequest.class);
            {
                final JsonNode params = serverRequest.getParams();
                assertNotNull(params);
                assertTrue(params.isObject());
                assertEquals(42, params.get("minuend").asInt());
                assertEquals(23, params.get("subtrahend").asInt());
                final NumericNode id = serverRequest.getId();
                assertNotNull(id);
                assertTrue(id.isIntegralNumber());
                assertEquals(4, id.asInt());
            }
        }
        {
            final NamedParametersServerRequest serverRequest
                    = readValueFromResource("named_parameters_02_request.json", NamedParametersServerRequest.class);
            {
                final JsonNode params = serverRequest.getParams();
                assertNotNull(params);
                assertTrue(params.isObject());
                assertEquals(42, params.get("minuend").asInt());
                assertEquals(23, params.get("subtrahend").asInt());
                final NumericNode id = serverRequest.getId();
                assertNotNull(id);
                assertTrue(id.isIntegralNumber());
                assertEquals(4, id.asInt());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(serverRequest);
            final NamedParametersClientRequest clientRequest
                    = OBJECT_MAPPER.readValue(string, NamedParametersClientRequest.class);
            {
                final SubtractParams params = clientRequest.getParams();
                assertNotNull(params);
                assertEquals(42, params.getMinuend());
                assertEquals(23, params.getSubtrahend());
                assertEquals(4, (int) clientRequest.getId());
            }
        }
    }

    @Test
    void named_parameters_01_response() throws IOException {
        {
            final NamedParametersClientResponse clientResponse = requireValid(readValueFromResource(
                    "named_parameters_01_response.json", NamedParametersClientResponse.class));
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(3, clientResponse.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientResponse);
            final NamedParametersServerResponse serverResponse
                    = requireValid(OBJECT_MAPPER.readValue(string, NamedParametersServerResponse.class));
            {
                assertEquals(19, (int) serverResponse.getResult());
                assertEquals(3, serverResponse.getId().asInt());
            }
        }
        {
            final NamedParametersServerResponse serverResponse = requireValid(readValueFromResource(
                    "named_parameters_01_response.json", NamedParametersServerResponse.class));
            {
                assertEquals(19, (int) serverResponse.getResult());
                assertEquals(3, serverResponse.getId().asInt());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(serverResponse);
            final NamedParametersClientResponse clientResponse
                    = OBJECT_MAPPER.readValue(string, NamedParametersClientResponse.class);
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(3, clientResponse.getId());
            }
        }
    }

    @Test
    void named_parameters_02_response() throws IOException {
        {
            final NamedParametersClientResponse clientResponse = requireValid(readValueFromResource(
                    "named_parameters_02_response.json", NamedParametersClientResponse.class));
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(4, clientResponse.getId());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(clientResponse);
            final NamedParametersServerResponse serverResponse
                    = requireValid(OBJECT_MAPPER.readValue(string, NamedParametersServerResponse.class));
            {
                assertEquals(19, (int) serverResponse.getResult());
                assertEquals(4, serverResponse.getId().asInt());
            }
        }
        {
            final NamedParametersServerResponse serverResponse = requireValid(readValueFromResource(
                    "named_parameters_02_response.json", NamedParametersServerResponse.class));
            {
                assertEquals(19, (int) serverResponse.getResult());
                assertEquals(4, serverResponse.getId().asInt());
            }
            final String string = OBJECT_MAPPER.writeValueAsString(serverResponse);
            final NamedParametersClientResponse clientResponse
                    = OBJECT_MAPPER.readValue(string, NamedParametersClientResponse.class);
            {
                assertEquals(19, (int) clientResponse.getResult());
                assertEquals(4, clientResponse.getId());
            }
        }
    }
}
