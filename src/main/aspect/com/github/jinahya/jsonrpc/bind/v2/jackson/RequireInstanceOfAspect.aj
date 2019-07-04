package com.github.jinahya.jsonrpc.bind.v2.jackson;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * An aspect for {@link RequireInstanceOf}.
 */
aspect RequireInstanceOfAspect {

    before(): execution(* *(.., @RequireInstanceOf (*), ..)) {
        final Signature signature = thisJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return;
        }
        final Method method = ((MethodSignature) signature).getMethod();
        final Parameter[] parameters = method.getParameters();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        final Object[] args = thisJoinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];
            for (final Annotation parameterAnnotation : parameterAnnotations[i]) {
                if (!(parameterAnnotation instanceof RequireInstanceOf)) {
                    continue;
                }
                final Class<?> clazz = ((RequireInstanceOf) parameterAnnotation).value();
                if (!clazz.isInstance(arg)) {
                    final Parameter parameter = parameters[i];
                    throw new IllegalArgumentException(
                            "arg(" + arg + ") of " + parameter.getType() + " at " + i + " named " + parameter.getName()
                            + " is not an instance of " + clazz);
                }
            }
        }
    }
}