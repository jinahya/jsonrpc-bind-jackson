package com.github.jinahya.jsonrpc.bind.v2;

public abstract class JacksonClientRequestTest<
        ObjectType extends JacksonClientRequest<ParamsType, IdType>, ParamsType, IdType>
        extends JacksonRequestTest<ObjectType, ParamsType, IdType> {

    public JacksonClientRequestTest(final Class<? extends ObjectType> objectClass,
                                    final Class<? extends ParamsType> paramsClass,
                                    final Class<? extends IdType> idClass) {
        super(objectClass, paramsClass, idClass);
    }
}
