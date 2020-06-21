package com.github.jinahya.jsonrpc.bind.v2.jackson;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcObject;

import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.jackson.IJsonrpcMessageHelper.unrecognizedProperties;
import static java.util.Objects.requireNonNull;

interface IJsonrpcObject extends JsonrpcObject {

    /**
     * Sets an entry of unrecognized property.
     *
     * @param key   the key of the unrecognized property.
     * @param value the value of the unrecognized property.
     * @return previous value mapped to the {@code key}.
     */
    @JsonAnySetter
    default Object putUnrecognizedProperty(final String key, final Object value) {
        requireNonNull(key, "key is null");
        return unrecognizedProperties(getClass(), this).put(key, value);
    }

    /**
     * Returns unrecognized properties.
     *
     * @return unrecognized properties.
     */
    @JsonAnyGetter
    default Map<String, Object> getUnrecognizedProperties() {
        return unrecognizedProperties(getClass(), this);
    }
}
