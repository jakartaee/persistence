/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.common.validation;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.TraversableResolver;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorContext;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.metadata.BeanDescriptor;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PersistenceTckValidatorFactory implements ValidatorFactory, Validator {

    private final List<ValidCallArgument> validCallArguments = new ArrayList<>();

    @Override
    public Validator getValidator() {
        return this;
    }

    @Override
    public ValidatorContext usingContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageInterpolator getMessageInterpolator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TraversableResolver getTraversableResolver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParameterNameProvider getParameterNameProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClockProvider getClockProvider() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        validCallArguments.add(new ValidCallArgument(object, groups));
        return Set.of();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanDescriptor getConstraintsForClass(Class<?> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExecutableValidator forExecutables() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {

    }

    public void assertValidCallsContain(ValidCallArgument... validCallArgument) {
        for (ValidCallArgument validCallArg : validCallArgument) {
            Assertions.assertAll();
            if (!validCallArguments.remove(validCallArg)) {
                throw new AssertionFailedError("Invalid call argument: " + validCallArg);
            }
        }
        validCallArguments.clear();
    }

    public void assertNoValidationCalls() {
        Assertions.assertTrue(validCallArguments.isEmpty());
    }

    public record ValidCallArgument(Object entity, Class<?>[] groups) {
    }

    public static ValidCallArgument validCall(Object entity, Class<?>... groups) {
        return new ValidCallArgument(entity, groups);
    }

    @Override
    public String toString() {
        return "PersistenceTckValidatorFactory{" +
                "validCallArguments=" + validCallArguments +
                '}';
    }
}
