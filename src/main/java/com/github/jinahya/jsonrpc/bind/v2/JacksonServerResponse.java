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

import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

import javax.validation.constraints.AssertTrue;

/**
 * An abstract class for server-side response object.
 *
 * @param <ResultType> result type parameter.
 * @param <ErrorType>  error type parameter.
 * @param <IdType>     id type parameter.
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public abstract class JacksonServerResponse<ResultType, ErrorType extends ErrorObject<?>, IdType extends ValueNode>
        extends JacksonResponse<ResultType, ErrorType, IdType> {

    /**
     * Check whether the current value of {@value #PROPERTY_NAME_ID} property is an instance of either {@link TextNode},
     * {@link NumericNode}, or {@link NullNode}.
     *
     * @return {@code true} if {@link #getId()} is {@code null} or is an instance of either {@link TextNode}, {@link
     * NumericNode}, or {@link NullNode}; {@code false} otherwise.
     */
    @AssertTrue(message = "a non-null id must be either TextNode, NumericNode, or NullNode")
    private boolean isIdEitherTextNumberOrNull() {
        final IdType id = getId();
        return id == null
               || id instanceof TextNode
               || id instanceof NumericNode
               || id instanceof NullNode;
    }
}
