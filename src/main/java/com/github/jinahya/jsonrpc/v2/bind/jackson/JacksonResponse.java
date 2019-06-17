package com.github.jinahya.jsonrpc.v2.bind.jackson;

import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

public class JacksonResponse<ResultType, ErrorType extends ErrorObject<?>>
        extends ResponseObject<ValueNode, ResultType, ErrorType> {

}
