package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequestTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PositionalParametersRequestTest extends JacksonRequestTest<PositionalParaemtersRequest, List<Integer>, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({"unchecked"})
    PositionalParametersRequestTest() {
        super(PositionalParaemtersRequest.class, (Class<List<Integer>>) (Class<?>) List.class, Integer.TYPE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void positional_parameters_01_request() throws IOException {
        final PositionalParaemtersRequest request = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_01_request.json");
        assertIterableEquals(asList(42, 23), request.getParams());
        assertEquals(1, request.getId().intValue());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void positional_parameters_02_request() throws IOException {
        final PositionalParaemtersRequest request = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_02_request.json");
        assertIterableEquals(asList(23, 42), request.getParams());
        assertEquals(2, request.getId().intValue());
    }
}
