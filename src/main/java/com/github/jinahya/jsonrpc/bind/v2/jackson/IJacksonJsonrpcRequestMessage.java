package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jinahya.jsonrpc.bind.v2a.IJsonrpcRequestMessage;
import com.github.jinahya.jsonrpc.glue.v2.jackson.IJacksonRequestMessageGlue;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public interface IJacksonJsonrpcRequestMessage extends IJsonrpcRequestMessage, IJacksonJsonrpcMessage {

    // ---------------------------------------------------------------------------------------------------------- method
    @Override
    default @NotBlank String getMethod() {
        final IJacksonRequestMessageGlue glue = IJacksonJsonrpcObjectHelper.glue(getClass(), this);
        return ofNullable(glue.getMethod()).map(JsonNode::asText).orElse(null);
    }

    @Override
    default void setMethod(final String method) {
        final IJacksonRequestMessageGlue glue = IJacksonJsonrpcObjectHelper.glue(getClass(), this);
        glue.setMethod(ofNullable(method).map(TextNode::new).orElse(null));
    }

    // ---------------------------------------------------------------------------------------------------------- params
    @Override
    default <T> List<T> getParamsAsList(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        final IJacksonRequestMessageGlue glue = IJacksonJsonrpcObjectHelper.glue(getClass(), this);
        final ContainerNode<?> params = glue.getParams();
        if (params == null || params.isNull()) {
            return null;
        }
        if (params instanceof ObjectNode) {
            final List<T> list = new ArrayList<>(1);
            list.add(getParamsAsObject(elementClass));
            return list;
        }
        assert params instanceof ArrayNode;
        final ObjectMapper mapper = IJacksonJsonrpcObjectHelper.mapper(getClass(), this);
        final ObjectReader reader = mapper.reader();
        return range(0, params.size())
                .mapToObj(params::get)
                .map(n -> {
                    try {
                        final T value = reader.readValue(n);
                        return value;
                    } catch (final IOException ioe) {
                        throw new UncheckedIOException(ioe);
                    }
                })
                .collect(toList());
    }

    @Override
    default <T> void setParamsAsList(final List<T> params) {
        final ObjectMapper mapper = IJacksonJsonrpcObjectHelper.mapper(getClass(), this);
        final IJacksonRequestMessageGlue glue = IJacksonJsonrpcObjectHelper.glue(getClass(), this);
        glue.setParams((ArrayNode) ofNullable(params).map(mapper::valueToTree).orElse(null));
    }

    @Override
    default <T> T getParamsAsObject(final Class<T> objectClass) {
        final IJacksonRequestMessageGlue glue = IJacksonJsonrpcObjectHelper.glue(getClass(), this);
        final ContainerNode<?> params = glue.getParams();
        if (params == null) {
            return null;
        }
        if (params instanceof ArrayNode) {
            final List<T> list = getParamsAsList(objectClass);
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
        assert params instanceof ObjectNode;
        final ObjectMapper mapper = IJacksonJsonrpcObjectHelper.mapper(getClass(), this);
        try {
            return mapper.reader().readValue(params);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    @Override
    default void setParamsAsObject(final Object params) {
        final IJacksonRequestMessageGlue glue = IJacksonJsonrpcObjectHelper.glue(getClass(), this);
        if (params == null) {
            glue.setParams(null);
            return;
        }
        if (params.getClass().isArray()) {
            setParamsAsList(stream((Object[]) params).collect(toList()));
            return;
        }
        final ObjectMapper mapper = IJacksonJsonrpcObjectHelper.mapper(getClass(), this);
        assert mapper != null;
        glue.setParams(mapper.valueToTree(params));
    }
}
