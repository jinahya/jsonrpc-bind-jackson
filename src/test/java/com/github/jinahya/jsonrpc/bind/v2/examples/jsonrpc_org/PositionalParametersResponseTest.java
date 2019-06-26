package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
class PositionalParametersResponseTest
        extends JacksonResponseTest<PositionalParametersResponse, Integer, ErrorObject<Object>, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({"unchecked"})
    PositionalParametersResponseTest() {
        super(PositionalParametersResponse.class, Integer.class,
              (Class<ErrorObject<Object>>) (Class<?>) ResponseObject.ErrorObject.class, Integer.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void named_parameters_01_response() throws IOException {
        {
            final PositionalParametersResponse response = readValueFromResource("named_parameters_01_response.json");
            log.debug("response: {}", response);
        }
    }

    @Test
    void named_parameters_02_response() throws IOException {
        {
            final PositionalParametersResponse response = readValueFromResource("named_parameters_02_response.json");
            log.debug("response: {}", response);
        }
    }
}
