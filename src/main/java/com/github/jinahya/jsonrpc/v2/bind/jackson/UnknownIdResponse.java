package com.github.jinahya.jsonrpc.v2.bind.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;

public class UnknownIdResponse<ResultType, ErrorType extends ResponseObject.ErrorObject<?>>
        extends ResponseObject<JsonNode, ResultType, ErrorType> {

}
