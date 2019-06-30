package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;

final class JacksonObjects {

    // -----------------------------------------------------------------------------------------------------------------
    static boolean isEitherStringNumberOfNull(final Object object) {
        return object instanceof TextNode || object instanceof NumericNode || object instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JacksonObjects() {
        super();
    }
}
