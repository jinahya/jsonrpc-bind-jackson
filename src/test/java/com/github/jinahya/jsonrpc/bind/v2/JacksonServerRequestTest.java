package com.github.jinahya.jsonrpc.bind.v2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public abstract class JacksonServerRequestTest<
        ObjectType extends JacksonServerRequest<IdType>, IdType extends ValueNode>
        extends JacksonRequestTest<ObjectType, JsonNode, IdType> {

    public JacksonServerRequestTest(final Class<? extends ObjectType> objectClass,
                                    final Class<? extends IdType> idClass) {
        super(objectClass, JsonNode.class, idClass);
    }
}
