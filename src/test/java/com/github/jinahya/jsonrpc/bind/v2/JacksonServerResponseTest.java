package com.github.jinahya.jsonrpc.bind.v2;

import com.fasterxml.jackson.databind.node.ValueNode;

public abstract class JacksonServerResponseTest<
        ObjectType extends JacksonServerResponse<ResultType, ErrorType, IdType>,
        ResultType,
        ErrorType extends ResponseObject.ErrorObject<?>,
        IdType extends ValueNode>
        extends JacksonResponseTest<ObjectType, ResultType, ErrorType, IdType> {

    public JacksonServerResponseTest(final Class<? extends ObjectType> objectClass,
                                     final Class<? extends ResultType> resultClass,
                                     final Class<? extends ErrorType> errorClass,
                                     final Class<? extends IdType> idClass) {
        super(objectClass, resultClass, errorClass, idClass);
    }
}
