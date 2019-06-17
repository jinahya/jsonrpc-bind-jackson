package com.github.jinahya.jsonrpc.v2.bind.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.RequestObject;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class JacksonRequest extends RequestObject<ValueNode, JsonNode> {

    /**
     * Returns current value of {@link #getParams()} mapped to specified type.
     *
     * @param objectMapper an object mapper.
     * @param paramsClass  the class to parse the {@value #PROPERTY_NAME_PARAMS} property.
     * @param <T>          value type parameter.
     * @return the value of {@value #PROPERTY_NAME_PARAMS} property mapped to specified params class.
     * @throws IOException if an I/O error occurs.
     */
    public <T> T getParams(final ObjectMapper objectMapper, final Class<? extends T> paramsClass)
            throws IOException {
        final JsonNode params = getParams();
        if (params == null) {
            return null;
        }
        final String valueString = objectMapper.writeValueAsString(params);
        return objectMapper.readValue(valueString, paramsClass);
    }

    public <T, R> R applyParams(final ObjectMapper objectMapper, final Class<? extends T> paramsClass,
                                final Function<? super T, ? extends R> paramsFunction)
            throws IOException {
        return paramsFunction.apply(getParams(objectMapper, paramsClass));
    }

    public <T, U, R> R applyParams(final ObjectMapper objectMapper, final Class<? extends T> paramsClass,
                                   final BiFunction<? super T, ? super U, ? extends R> paramsFunction,
                                   final Supplier<? extends U> argumentSupplier)
            throws IOException {
        return applyParams(objectMapper, paramsClass, v -> paramsFunction.apply(v, argumentSupplier.get()));
    }

    public <T> void acceptParams(final ObjectMapper objectMapper, final Class<? extends T> paramsClass,
                                 final Consumer<? super T> paramsConsumer)
            throws IOException {
        applyParams(objectMapper, paramsClass, v -> {
            paramsConsumer.accept(v);
            return null;
        });
    }

    public <T, U> void acceptParams(final ObjectMapper objectMapper, final Class<? extends T> paramsClass,
                                    final BiConsumer<? super T, ? super U> paramsConsumer,
                                    final Supplier<? extends U> argumentSupplier)
            throws IOException {
        acceptParams(objectMapper, paramsClass, v -> paramsConsumer.accept(v, argumentSupplier.get()));
    }
}
