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
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

import javax.validation.constraints.AssertTrue;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonUtils.isEitherTextNumberOrNull;

/**
 * A base class for server-side response object.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonServerResponse extends JacksonResponse<JsonNode, ErrorObject<JsonNode>, ValueNode> {

    // -----------------------------------------------------------------------------------------------------------------
    @AssertTrue(message = "a non-null id must be either TextNode, NumericNode, or NullNode")
    private boolean isIdEitherTextNumberOrNull() {
        final ValueNode id = getId();
        return id == null || isEitherTextNumberOrNull(id);
    }
}
