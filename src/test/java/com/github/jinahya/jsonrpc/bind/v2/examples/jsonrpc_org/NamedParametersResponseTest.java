package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonErrorTest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse.JacksonError;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponseTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamedParametersResponseTest
        extends JacksonResponseTest<NamedParametersResponse, Integer, JacksonError<Void>, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    NamedParametersResponseTest() {
        super(NamedParametersResponse.class, Integer.class, JacksonErrorTest.NoData.class, Integer.TYPE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void named_parameters_01_response() throws IOException {
        final NamedParametersResponse response = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_01_response.json");
        assertEquals(19, response.getResult().intValue());
        assertEquals(3, response.getId().intValue());
    }

    @Test
    void named_parameters_02_response() throws IOException {
        final NamedParametersResponse response = readValueFromResource(
                "/com/github/jinahya/jsonrpc/bind/v2/examples/jsonrpc_org/named_parameters_02_response.json");
        assertEquals(19, response.getResult().intValue());
        assertEquals(4, response.getId().intValue());
    }
}
