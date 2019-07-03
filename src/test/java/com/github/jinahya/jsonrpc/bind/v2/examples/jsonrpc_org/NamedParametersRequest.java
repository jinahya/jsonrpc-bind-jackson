package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org.NamedParametersRequest.SubtractParams;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonRequest;
import lombok.Data;

class NamedParametersRequest extends JacksonRequest<SubtractParams, Integer> {

    // -----------------------------------------------------------------------------------------------------------------
    @Data
    public static class SubtractParams {

        private int subtrahend;

        private int minuend;
    }
}
