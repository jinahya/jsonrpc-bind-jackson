package com.github.jinahya.jsonrpc.bind.v2;

public abstract class JacksonRequestTest<ObjectType extends JacksonRequest<ParamsType, IdType>, ParamsType, IdType>
        extends RequestObjectTest<ObjectType, ParamsType, IdType> {

    public JacksonRequestTest(final Class<? extends ObjectType> objectClass,
                              final Class<? extends ParamsType> paramsClass, final Class<? extends IdType> idClass) {
        super(objectClass, paramsClass, idClass);
    }
}
