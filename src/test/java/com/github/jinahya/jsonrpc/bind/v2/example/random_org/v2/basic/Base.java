package com.github.jinahya.jsonrpc.bind.v2.example.random_org.v2.basic;

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

public enum Base {

    BINARY(2),

    OCTAL(8),

    DECIMAL(10),

    HEXADECIMAL(16);

    public static Base valueOfBase(final int base) {
        for (final Base value : values()) {
            if (value.base == base) {
                return value;
            }
        }
        throw new IllegalArgumentException("no value for base: " + base);
    }

    Base(final int base) {
        this.base = base;
    }

    public int getBase() {
        return base;
    }

    private final int base;
}
