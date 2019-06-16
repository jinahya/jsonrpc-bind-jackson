package com.github.jinahya.jsonrpc.v2.bind.jackson;

import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;

public class UnknownIdResponse<ResultType, ErrorType extends ResponseObject.ErrorObject<?>>
        extends ResponseObject<ValueNode, ResultType, ErrorType> {

}
