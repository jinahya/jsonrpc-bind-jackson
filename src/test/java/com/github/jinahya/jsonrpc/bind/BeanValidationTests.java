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

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BeanValidationTests {

    // -----------------------------------------------------------------------------------------------------------------
//    public static final ValidatorFactory VALIDATION_FACTORY = Validation.buildDefaultValidatorFactory();

    // https://stackoverflow.com/a/54750045/330457
    public static final ValidatorFactory VALIDATION_FACTORY
            = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    public static <R> R applyValidator(final Function<? super Validator, ? extends R> function) {
        return function.apply(VALIDATION_FACTORY.getValidator());
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <U, R> R applyValidator(final Supplier<? extends U> supplier,
                                          final BiFunction<? super Validator, ? super U, ? extends R> function) {
        return applyValidator(v -> function.apply(v, supplier.get()));
    }

    public static void acceptValidator(final Consumer<? super Validator> consumer) {
        applyValidator(v -> {
            consumer.accept(v);
            return null;
        });
    }

    public static <U> void acceptValidator(final Supplier<? extends U> supplier,
                                           final BiConsumer<? super Validator, ? super U> consumer) {
        acceptValidator(v -> consumer.accept(v, supplier.get()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    public static <T> Set<ConstraintViolation<T>> validate(final T object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        return applyValidator(v -> v.validate(object));
    }

    public static <T> T requireValid(final T object) {
        if (object == null) {
            throw new NullPointerException("object is null");
        }
        final Set<ConstraintViolation<T>> violations = validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return object;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private BeanValidationTests() {
        super();
    }
}
