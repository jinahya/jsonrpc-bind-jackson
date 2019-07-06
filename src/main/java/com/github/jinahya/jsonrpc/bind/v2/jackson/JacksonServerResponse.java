package com.github.jinahya.jsonrpc.bind.v2.jackson;

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
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonResponse.JacksonError.JacksonServerError;

import static java.util.Optional.ofNullable;

/**
 * A class for lazily mappable response objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonServerResponse extends JacksonResponse<JsonNode, JacksonServerError, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance whose properties are set from specified node.
     *
     * @param node the node from which properties are set.
     * @return a new instance.
     */
    public static JacksonServerResponse of(final JsonNode node) {
        final String jsonrpc = ofNullable(node.get(PROPERTY_NAME_JSONRPC)).map(JsonNode::asText).orElse(null);
        final JsonNode result = node.get(PROPERTY_NAME_RESULT);
        final JacksonServerError error
                = ofNullable(node.get(PROPERTY_NAME_ERROR)).map(JacksonServerError::of).orElse(null);
        final ValueNode id = (ValueNode) node.get(PROPERTY_NAME_ID);
        return of(JacksonServerResponse.class, jsonrpc, result, error, id);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public JacksonServerResponse() {
        super();
    }
}
