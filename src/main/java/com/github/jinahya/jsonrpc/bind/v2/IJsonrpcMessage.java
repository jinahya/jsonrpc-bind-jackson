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

import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;

import javax.validation.constraints.AssertTrue;
import java.math.BigInteger;

import static com.github.jinahya.jsonrpc.bind.v2.IJsonrpcMessageHelper.setId;
import static com.github.jinahya.jsonrpc.bind.v2.IJsonrpcObjectHelper.hasOneThenEvaluateOrFalse;
import static com.github.jinahya.jsonrpc.bind.v2.IJsonrpcObjectHelper.hasOneThenEvaluateOrTrue;
import static com.github.jinahya.jsonrpc.bind.v2.IJsonrpcObjectHelper.hasOneThenMapOrNull;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcObjectHelper.evaluatingTrue;
import static java.util.Optional.ofNullable;

interface IJsonrpcMessage<S extends IJsonrpcMessage<S>>
        extends IJsonrpcObject<S>,
                JsonrpcMessage {

    @Override
    default boolean hasId() {
        return hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IJsonrpcMessageHelper::getId,
                evaluatingTrue()
        );
    }

    @Override
    default @AssertTrue boolean isIdContextuallyValid() {
        return hasOneThenEvaluateOrTrue(
                getClass(),
                this,
                IJsonrpcMessageHelper::getId,
                id -> id.isTextual() || id.isIntegralNumber()
        );
    }

    @Override
    default String getIdAsString() {
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getId,
                ValueNode::asText
        );
    }

    @Override
    default void setIdAsString(final String id) {
        setId(getClass(), this, ofNullable(id).map(TextNode::new).orElse(null));
    }

    @Override
    default BigInteger getIdAsNumber() {
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getId,
                id -> {
                    if (id.isNumber()) {
                        return id.bigIntegerValue(); // BigInteger.ZERO <- !isNumber()
                    }
                    try {
                        return new BigInteger(getIdAsString());
                    } catch (final NumberFormatException nfe) {
                        // suppressed
                    }
                    throw new JsonrpcBindException("unable to bind id as a number");
                }
        );
    }

    @Override
    default void setIdAsNumber(final BigInteger id) {
        setId(getClass(), this, ofNullable(id).map(BigIntegerNode::valueOf).orElse(null));
    }

    @Override
    default Long getIdAsLong() {
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getId,
                id -> {
                    if (id.canConvertToLong()) {
                        return id.longValue();
                    }
                    return JsonrpcMessage.super.getIdAsLong();
                }
        );
    }

    @Override
    default void setIdAsLong(final Long id) {
        setId(getClass(), this, ofNullable(id).map(LongNode::new).orElse(null));
    }

    @Override
    default Integer getIdAsInteger() {
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getId,
                id -> {
                    if (id.canConvertToInt()) {
                        return id.intValue();
                    }
                    return JsonrpcMessage.super.getIdAsInteger();
                }
        );
    }

    @Override
    default void setIdAsInteger(final Integer id) {
        setId(getClass(), this, ofNullable(id).map(IntNode::new).orElse(null));
    }
}

