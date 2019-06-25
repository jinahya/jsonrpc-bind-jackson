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
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

import javax.validation.constraints.AssertTrue;

/**
 * A base class for server-side response objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonServerResponse extends JacksonResponse<JsonNode, ErrorObject<JsonNode>, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------
    public static class JacksonServerError extends ErrorObject<JsonNode> {

    }
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public JacksonServerResponse() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Check whether the current value of {@value #PROPERTY_NAME_ID} property is an instance of either {@link TextNode},
     * {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #getId()} is {@code null} or is an instance of either {@link TextNode}, {@link
     * NumericNode}, or {@link NullNode}; {@code false} otherwise.
     */
    @AssertTrue//(message = "a non-null id must be either TextNode, NumericNode, or NullNode")
    private boolean isIdEitherTextNodeNumericNodeOrNullNode() {
        final ValueNode id = getId();
        return id == null || id instanceof TextNode || id instanceof NumericNode || id instanceof NullNode;
    }
}
