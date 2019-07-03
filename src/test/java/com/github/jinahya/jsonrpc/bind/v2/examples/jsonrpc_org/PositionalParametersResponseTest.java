package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonErrorTest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse.JacksonError;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponseTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionalParametersResponseTest
        extends JacksonResponseTest<PositionalParametersResponse, Integer, JacksonError<Void>, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    PositionalParametersResponseTest() {
        super(PositionalParametersResponse.class, Integer.class, JacksonErrorTest.NoData.class, Integer.TYPE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void positional_parameters_01_response() throws IOException {
        final PositionalParametersResponse response = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_01_response.json");
        assertEquals(19, response.getResult().intValue());
        assertEquals(1, response.getId().intValue());
    }

    @Test
    void positional_parameters_02_response() throws IOException {
        final PositionalParametersResponse response = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/positional_parameters_02_response.json");
        assertEquals(-19, response.getResult().intValue());
        assertEquals(2, response.getId().intValue());
    }
}
