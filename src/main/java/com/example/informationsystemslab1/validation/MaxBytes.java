package com.example.informationsystemslab1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxBytesValidator.class)
public @interface MaxBytes {

    int max();

    String message() default "длина не должна превышать {max} байт в UTF-8";

    // обязательные по контракту Bean Validation
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
