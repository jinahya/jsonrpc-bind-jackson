package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.jinahya.jsonrpc.bind.JacksonTests;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse.JacksonServerError;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidRequestTest {

    @Test
    void invalid_request_object_01_response() throws IOException {
        {
            final JavaType paramsType = OBJECT_MAPPER.getTypeFactory().constructParametricType(
                    JacksonResponse.class, JsonNode.class, JacksonServerError.class, JsonNode.class);
            final JacksonResponse<JsonNode, ErrorObject<JsonNode>, JsonNode> response
                    = JacksonTests.readValueFromResource(
                    "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/invalid_request_object_01_response.json",
                    paramsType);
            assertEquals(ErrorObject.CODE_INVALID_REQUEST, response.getError().getCode());
            assertEquals("Invalid Request", response.getError().getMessage());
        }
    }
}
