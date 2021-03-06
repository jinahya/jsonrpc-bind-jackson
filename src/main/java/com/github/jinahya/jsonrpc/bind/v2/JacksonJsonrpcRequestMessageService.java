package com.github.jinahya.jsonrpc.bind.v2;

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

import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcMessageServiceHelper.readValue;
import static com.github.jinahya.jsonrpc.bind.v2.JacksonJsonrpcMessageServiceHelper.writeValue;
import static java.util.Objects.requireNonNull;

/**
 * A class implements {@link JsonrpcRequestMessageService} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class JacksonJsonrpcRequestMessageService implements JsonrpcRequestMessageService {

    @Override
    public JsonrpcRequestMessage fromJson(final Object source) {
        requireNonNull(source, "source is null");
        return readValue(source, JacksonJsonrpcRequestMessage.class);
    }

    @Override
    public void toJson(final JsonrpcRequestMessage message, final Object target) {
        requireNonNull(message, "message is null");
        requireNonNull(target, "target is null");
        writeValue(target, message);
    }
}
