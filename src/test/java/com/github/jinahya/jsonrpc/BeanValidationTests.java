package com.github.jinahya.jsonrpc;

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

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static javax.validation.Validation.byDefaultProvider;

/**
 * A utility class for Bean-Validation.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class BeanValidationTests {

    /**
     * An instance of (thread-safe) {@link ValidatorFactory}.
     */
    private static ValidatorFactory VALIDATOR_FACTORY;

    /**
     * Returns a validator factory.
     *
     * @return a validator factory.
     */
    public static ValidatorFactory getValidatorFactory() {
        if (VALIDATOR_FACTORY == null) {
            // https://stackoverflow.com/a/54750045/330457
            VALIDATOR_FACTORY
                    = byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory();
        }
        return VALIDATOR_FACTORY;
    }

    /**
     * Returns an instance of {@link Validator}.
     *
     * @return an instance of {@link Validator}
     * @see #getValidatorFactory()
     */
    public static Validator getValidator() {
        return getValidatorFactory().getValidator();
    }

    /**
     * Validates specified object.
     *
     * @param object the object to validate.
     * @param groups the group of list of groups targeted for validation.
     * @param <T>    bean type parameter
     * @return a set of {@link ConstraintViolation} which might be empty if there is no constraint violations.
     * @see #getValidator()
     * @see Validator#validate(Object, Class[])
     */
    public static <T> Set<ConstraintViolation<T>> validate(final T object, final Class<?>... groups) {
        return getValidator().validate(object, groups);
    }

    /**
     * Checks that specified object is valid. This method invokes {@link #validate(Object, Class[])} with given
     * arguments and throws an {@link ConstraintViolationException} if the result set is not empty.
     *
     * @param object the object to validate.
     * @param groups the group of list of groups targeted for validation.
     * @param <T>    bean type parameter
     * @return given bean.
     * @see #validate(Object, Class[])
     */
    public static <T> T requireValid(final T object, final Class<?>... groups) {
        final Set<ConstraintViolation<T>> violations = validate(object, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return object;
    }

    /**
     * Creates a new instance, my ass.
     */
    private BeanValidationTests() {
        throw new AssertionError("instantiation is not allowed");
    }
}
