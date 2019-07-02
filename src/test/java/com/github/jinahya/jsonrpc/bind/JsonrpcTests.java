package com.github.jinahya.jsonrpc.bind;

/*-
 * #%L
 * jsonrpc-bind
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

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Slf4j
public final class JsonrpcTests {

    // -----------------------------------------------------------------------------------------------------------------
    public static <R> R applyResourceStream(final String name,
                                            final Function<? super InputStream, ? extends R> function)
            throws IOException {
        try (InputStream resourceStream = JsonrpcTests.class.getResourceAsStream(name)) {
            assertNotNull(resourceStream, "null resource stream for '" + name + "'");
            return function.apply(resourceStream);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private JsonrpcTests() {
        super();
    }
}
