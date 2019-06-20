package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import javax.validation.constraints.AssertTrue;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An abstract class for server-side request object.
 *
 * @param <IdType> id type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public abstract class JacksonServerRequest<IdType extends ValueNode> extends JacksonRequest<JsonNode, IdType> {

    /**
     * Checks whether the current value of {@link #PROPERTY_NAME_PARAMS} property is an instance of either {@link
     * ArrayNode}, {@link ObjectNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #PROPERTY_NAME_PARAMS} property is an instance of either {@link ArrayNode}, {@link
     * * ObjectNode}, or {@link NullNode}; {@code false} otherwise.
     */
    @AssertTrue
    private boolean isParamsEitherArrayObjectOrNull() {
        final JsonNode params = getParams();
        return params == null
               || params instanceof ArrayNode
               || params instanceof ObjectNode
               || params instanceof NullNode;
    }

    /**
     * Check whether the current value of {@value #PROPERTY_NAME_ID} property is an instance of either {@link TextNode},
     * {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #getId()} is {@code null} or is an instance of either {@link TextNode}, {@link
     * NumericNode}, or {@link NullNode}; {@code false} otherwise.
     */
    @AssertTrue
    private boolean isIdEitherTextNumberOrNull() {
        final IdType id = getId();
        return id == null
               || id instanceof TextNode
               || id instanceof NumericNode
               || id instanceof NullNode;
    }

    /**
     * Returns current value of {@link #getParams()} translated to specified type.
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
