package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.JacksonTests;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequestTest;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.JacksonTests.OBJECT_MAPPER;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@Slf4j
class PositionalParametersRequestTest extends JacksonRequestTest<PositionalParametersRequest, List<Integer>, Integer> {

    @SuppressWarnings({"unchecked"})
    PositionalParametersRequestTest() {
        super(PositionalParametersRequest.class, (Class<List<Integer>>) (Class<?>) List.class, Integer.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void positional_parameters_01_request() throws IOException {
        {
            final PositionalParametersRequest request = readValueFromResource("positional_parameters_01_request.json");
            assertIterableEquals(asList(42, 23), request.getParams());
            assertEquals(1, request.getId());
        }
        {
            final JacksonServerRequest request = JacksonTests.readValueFromResource(
                    "positional_parameters_01_request.json", JacksonServerRequest.class, getClass());
            final List<Integer> params = request.getParamsAsPositioned(OBJECT_MAPPER, Integer.class, new ArrayList<>());
            assertIterableEquals(asList(42, 23), params);
            assertEquals(1, request.getId().asInt());
        }
    }

    @Test
    void positional_parameters_02_request() throws IOException {
        {
            final PositionalParametersRequest request = readValueFromResource("positional_parameters_02_request.json");
            assertIterableEquals(asList(23, 42), request.getParams());
            assertEquals(2, request.getId());
        }
        {
            final JacksonServerRequest request = JacksonTests.readValueFromResource(
                    "positional_parameters_02_request.json", JacksonServerRequest.class, getClass());
            final List<Integer> params = request.getParamsAsPositioned(OBJECT_MAPPER, Integer.class, new ArrayList<>());
            assertIterableEquals(asList(23, 42), params);
            assertEquals(2, request.getId().asInt());
        }
    }
}
