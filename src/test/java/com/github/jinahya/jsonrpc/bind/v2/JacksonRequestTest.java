package com.github.jinahya.jsonrpc.bind.v2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public abstract class JacksonRequestTest<ObjectType extends JacksonRequest<IdType>, IdType extends ValueNode>
        extends RequestObjectTest<ObjectType, JsonNode, IdType> {

    public JacksonRequestTest(final Class<? extends ObjectType> objectClass, final Class<? extends IdType> idClass) {
        super(objectClass, JsonNode.class, idClass);
    }
}
