package com.borani.spring.boot.hibernate.generator;


import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@IdGeneratorType(BigIntegerSequenceGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface BigIntegerGeneratedId {
}