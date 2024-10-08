package com.example.api.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Constraint(validatedBy = AfterDateOfFirstFilmValidator.class)
@Documented
@Retention(RUNTIME)
@Target({FIELD})
public @interface AfterDateOfFirstFilm {

    String message() default AfterDateOfFirstFilmValidator.MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
