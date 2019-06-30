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

import com.github.jinahya.jsonrpc.bind.JacksonTests;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * An abstract class for testing subclsses of {@link JacksonRequest}.
 *
 * @param <ObjectType> subclass type parameter
 * @param <ParamsType> params type parameter
 * @param <IdType>     id type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public abstract class JacksonRequestTest<ObjectType extends JacksonRequest<ParamsType, IdType>, ParamsType, IdType> {

    // -----------------------------------------------------------------------------------------------------------------
    public JacksonRequestTest(final Class<? extends ObjectType> objectClass,
                              final Class<? extends ParamsType> paramsClass, final Class<? extends IdType> idClass) {
        super();
        this.objectClass = requireNonNull(objectClass, "objectClass is null");
        this.paramsClass = requireNonNull(paramsClass, "paramsClass is null");
        this.idClass = requireNonNull(idClass, "idClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected ObjectType readValueFromResource(final String name) throws IOException {
        return JacksonTests.readValueFromResource(name, objectClass);
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected final Class<? extends ObjectType> objectClass;

    protected final Class<? extends ParamsType> paramsClass;

    protected final Class<? extends IdType> idClass;
}
