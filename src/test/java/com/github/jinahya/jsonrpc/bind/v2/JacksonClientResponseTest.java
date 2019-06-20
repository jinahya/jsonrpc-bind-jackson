package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

public abstract class JacksonClientResponseTest<
        ObjectType extends JacksonClientResponse<ResultType, ErrorType, IdType>,
        ResultType,
        ErrorType extends ErrorObject<?>,
        IdType>
        extends ResponseObjectTest<ObjectType, ResultType, ErrorType, IdType> {

    public JacksonClientResponseTest(final Class<? extends ObjectType> objectClass,
                                     final Class<? extends ResultType> resultClass,
                                     final Class<? extends ErrorType> errorClass,
                                     final Class<? extends IdType> idClass) {
        super(objectClass, resultClass, errorClass, idClass);
    }
}
