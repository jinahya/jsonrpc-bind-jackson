package com.github.jinahya.jsonrpc.v2.bind.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class UnknownParamsRequest<IdType> extends RequestObject<IdType, JsonNode> {

    public <T> T getParams(final Supplier<ObjectMapper> mapperSupplier, final Class<? extends T> paramsClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null) {
            return null;
        }
        final ObjectMapper mapper = mapperSupplier.get();
        final String string = mapper.writeValueAsString(params);
        return mapperSupplier.get().readValue(string, paramsClass);
    }

    public <T, R> R applyParams(final Supplier<ObjectMapper> mapperSupplier, final Class<? extends T> paramsClass,
                                final Function<? super T, ? extends R> paramsFunction)
            throws IOException {
        return paramsFunction.apply(getParams(mapperSupplier, paramsClass));
    }

    public <T, U, R> R applyParams(final Supplier<ObjectMapper> mapperSupplier, final Class<? extends T> paramsClass,
                                   final BiFunction<? super T, ? super U, ? extends R> paramsFunction,
                                   final Supplier<? extends U> argumentSupplier)
            throws IOException {
        return applyParams(mapperSupplier, paramsClass, v -> paramsFunction.apply(v, argumentSupplier.get()));
    }

    public <T> void acceptParams(final Supplier<ObjectMapper> mapperSupplier, final Class<? extends T> paramsClass,
                                 final Consumer<? super T> paramsConsumer)
            throws IOException {
        applyParams(mapperSupplier, paramsClass, v -> {
            paramsConsumer.accept(v);
            return null;
        });
    }

    public <T, U> void acceptParams(final Supplier<ObjectMapper> mapperSupplier, final Class<? extends T> paramsClass,
                                    final BiConsumer<? super T, ? super U> paramsConsumer,
                                    final Supplier<? extends U> argumentSupplier)
            throws IOException {
        acceptParams(mapperSupplier, paramsClass, v -> paramsConsumer.accept(v, argumentSupplier.get()));
    }
}
