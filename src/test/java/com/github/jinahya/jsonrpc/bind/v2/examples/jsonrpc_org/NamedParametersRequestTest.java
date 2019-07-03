package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org.NamedParametersRequest.SubtractParams;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequestTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NamedParametersRequestTest extends JacksonRequestTest<NamedParametersRequest, SubtractParams, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    NamedParametersRequestTest() {
        super(NamedParametersRequest.class, SubtractParams.class, Integer.TYPE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void named_parameters_01_request() throws IOException {
        final NamedParametersRequest request = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_request.json");
        final SubtractParams params = request.getParams();
        assertNotNull(params);
        assertEquals(23, params.getSubtrahend());
        assertEquals(42, params.getMinuend());
        assertEquals(3, request.getId().intValue());
    }

    @Test
    void named_parameters_02_request() throws IOException {
        final NamedParametersRequest request = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_request.json");
        final SubtractParams params = request.getParams();
        assertNotNull(params);
        assertEquals(23, params.getSubtrahend());
        assertEquals(42, params.getMinuend());
        assertEquals(4, request.getId().intValue());
    }
}
