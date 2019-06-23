package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerRequest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.BeanValidationUtils.requireValid;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static com.github.jinahya.jsonrpc.bind.JacksonTests.readTreeFromResource;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class BatchTest {

    @Test
    void batch_01_request() throws IOException {
        final JsonNode array = readTreeFromResource("batch_01_request.json", getClass());
        {
            final JsonNode element = array.get(0);
            final JacksonServerRequest request
                    = requireValid(OBJECT_MAPPER.treeToValue(element, JacksonServerRequest.class));
            assertEquals("sum", request.getMethod());
            final List<Integer> params = request.getParamsAsPositioned(OBJECT_MAPPER, Integer.class);
            assertIterableEquals(asList(1, 2, 4), params);
            assertEquals("1", request.getId().asText());
        }
        {
            final JsonNode element = array.get(1);
            final JacksonServerRequest request = requireValid(OBJECT_MAPPER.treeToValue(
                    element, JacksonServerRequest.class));
            assertEquals("notify_hello", request.getMethod());
            final List<Integer> params = request.getParamsAsPositioned(OBJECT_MAPPER, Integer.class);
            assertIterableEquals(singletonList(7), params);
            assertNull(request.getId());
        }
        {
            final JsonNode element = array.get(2);
            final JacksonServerRequest request = requireValid(OBJECT_MAPPER.treeToValue(
                    element, JacksonServerRequest.class));
            assertEquals("subtract", request.getMethod());
            final List<Integer> params = request.getParamsAsPositioned(OBJECT_MAPPER, Integer.class);
            assertIterableEquals(asList(42, 23), params);
            assertEquals("2", request.getId().asText());
        }
        {
            final JsonNode element = array.get(3);
            assertThrows(JsonProcessingException.class, () -> {
                final JacksonServerRequest request = requireValid(OBJECT_MAPPER.treeToValue(
                        element, JacksonServerRequest.class));
            });
        }
        {
            final JsonNode element = array.get(4);
            final JacksonServerRequest request = requireValid(OBJECT_MAPPER.treeToValue(
                    element, JacksonServerRequest.class));
            assertEquals("foo.get", request.getMethod());
            final JsonNode params = request.getParams();
            assertEquals("myself", params.get("name").asText());
            assertEquals("5", request.getId().asText());
        }
        {
            final JsonNode element = array.get(5);
            final JacksonServerRequest request = requireValid(OBJECT_MAPPER.treeToValue(
                    element, JacksonServerRequest.class));
            assertEquals("get_data", request.getMethod());
            assertNull(request.getParams());
            assertEquals("9", request.getId().asText());
        }
    }

    @Test
    void batch_01_response() throws IOException {
        final JsonNode array = readTreeFromResource("batch_01_response.json", getClass());
        {
            final JsonNode element = array.get(0);
            final JacksonServerResponse response
                    = requireValid(OBJECT_MAPPER.treeToValue(element, JacksonServerResponse.class));
            assertEquals(7, response.getResult().asInt());
            assertEquals("1", response.getId().asText());
        }
    }
}
