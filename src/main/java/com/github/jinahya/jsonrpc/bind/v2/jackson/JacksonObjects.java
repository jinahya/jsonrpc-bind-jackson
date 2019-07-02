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

import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Constants and utilities for Jackson objects.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
final class JacksonObjects {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Checks whether specified object is either an instance of {@link TextNode}, {@link NumericNode}, {@link
     * NullNode}.
     *
     * @param object the object to to check
     * @return {@true} is specified object is either an instance of {@link TextNode}, {@link NumericNode}, {@link
     * NullNode}; {@code false} otherwise.
     */
    static boolean isEitherStringNumberOfNull(final Object object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        return object instanceof TextNode || object instanceof NumericNode || object instanceof NullNode;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    private JacksonObjects() {
        super();
    }
}
