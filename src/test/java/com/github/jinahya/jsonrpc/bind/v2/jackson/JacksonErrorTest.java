package com.github.jinahya.jsonrpc.bind.v2.jackson;

import static java.util.Objects.requireNonNull;

public abstract class JacksonErrorTest<T extends JacksonResponse.JacksonError<DataType>, DataType> {

    // -----------------------------------------------------------------------------------------------------------------
    public static class NoData extends JacksonResponse.JacksonError<Void> {

    }

    // -----------------------------------------------------------------------------------------------------------------
    public JacksonErrorTest(final Class<? extends T> objectClass, final Class<? extends DataType> dataClass) {
        super();
        this.objectClass = requireNonNull(objectClass, "objectClass is null");
        this.dataClass = requireNonNull(dataClass, "dataClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<? extends T> objectClass;

    protected final Class<? extends DataType> dataClass;
}
