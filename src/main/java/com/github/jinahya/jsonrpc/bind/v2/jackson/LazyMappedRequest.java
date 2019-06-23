package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import javax.validation.constraints.AssertTrue;
import java.io.IOException;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonUtils.isEitherTextNumberOrNull;
import static java.util.Optional.ofNullable;

public class LazyMappedRequest extends JacksonServerRequest<ValueNode> {

//    // -----------------------------------------------------------------------------------------------------------------
//    @AssertTrue(message = "a non-null params must be either ArrayNode, ObjectNode, or NullNode")
//    private boolean isParamsEitherArrayObjectOrNull() {
//        final JsonNode params = getParams();
//        return params == null
//               || params instanceof ArrayNode || params instanceof ObjectNode || params instanceof NullNode;
//    }
//
//    @AssertTrue(message = "a non-null id must be either TextNode, NumericNode, or NullNode")
//    private boolean isIdEitherTextNumberOrNull() {
//        final ValueNode id = getId();
//        return id == null || isEitherTextNumberOrNull(id);
//    }
//
//    // -----------------------------------------------------------------------------------------------------------------
//    public <T> List<T> getParamsAsPositioned(final ObjectMapper objectMapper, final Class<?> elementClass)
//            throws IOException {
//        final JsonNode params = getParams();
//        if (params == null) {
//            throw new IllegalStateException("params is currently null");
//        }
//        if (params instanceof NullNode) {
//            return null;
//        }
//        if (!(params instanceof ArrayNode)) {
//            throw new IllegalStateException("params(" + params + ") is not an instance of " + ArrayNode.class);
//        }
//        final String valueString = objectMapper.writeValueAsString(params);
//        final CollectionType collectionType
//                = objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass);
//        return objectMapper.readValue(valueString, collectionType);
//    }
//
//    /**
//     * Returns current value of {@link #getParams()} translated to specified type.
//     *
//     * @param objectMapper an object mapper.
//     * @param paramsClass  the class to parse the {@value #PROPERTY_NAME_PARAMS} property.
//     * @param <T>          value type parameter.
//     * @return the value of {@value #PROPERTY_NAME_PARAMS} property mapped to specified params class.
//     * @throws IOException if an I/O error occurs.
//     */
//    public <T> T getParamsAsNamed(final ObjectMapper objectMapper, final Class<? extends T> paramsClass)
//            throws IOException {
//        final JsonNode params = getParams();
//        if (params == null) {
//            throw new IllegalStateException("params is currently null");
//        }
//        if (params instanceof NullNode) {
//            return null;
//        }
//        if (!(params instanceof ObjectNode)) {
//            throw new IllegalStateException("params(" + params + ") is not an instance of " + ObjectNode.class);
//        }
//        final String valueString = objectMapper.writeValueAsString(params);
//        return objectMapper.readValue(valueString, paramsClass);
//    }
//
//    @Deprecated
//    public Object getParams(final ObjectMapper objectMapper, final Class<?> namedObjectClass,
//                            final Class<?> positionedElementClass)
//            throws IOException {
//        final JsonNode params = getParams();
//        if (params == null) {
//            throw new IllegalStateException("params property is currently null");
//        }
//        if (params instanceof NullNode) {
//            return null;
//        }
//        if (params instanceof ArrayNode) {
//            return getParamsAsPositioned(objectMapper, positionedElementClass);
//        }
//        if (params instanceof ObjectNode) {
//            return getParamsAsNamed(objectMapper, namedObjectClass);
//        }
//        throw new IllegalStateException("unknown param type: " + params);
//    }

//    // -----------------------------------------------------------------------------------------------------------------
//    public void setParamsAsPositioned(final ObjectMapper objectMapper, final List<?> paramsValue) {
//        final ArrayNode params = objectMapper.createArrayNode();
//        for (final Object paramsElement : paramsValue) {
//            params.add(objectMapper.valueToTree(paramsElement));
//        }
//        setParams(params);
//    }
//
//    public void setParamsAsNamed(final ObjectMapper objectMapper, final Object paramsValue) {
//        setParams(ofNullable(paramsValue).map(v -> (JsonNode) objectMapper.valueToTree(v)).orElse(null));
//        if (paramsValue == null) {
//            setParams(NullNode.getInstance());
//            return;
//        }
//        setParams(objectMapper.valueToTree(paramsValue));
//    }
}
