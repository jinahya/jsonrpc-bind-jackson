package com.github.jinahya.jsonrpc.v2.bind.jackson;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject;

public class LazyResponse<ResultType, ErrorType extends ResponseObject.ErrorObject<?>>
        extends UnknownIdResponse<ResultType, ErrorType> {

}
