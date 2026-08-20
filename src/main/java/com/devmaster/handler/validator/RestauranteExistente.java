package com.devmaster.handler.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RestauranteExistenteValidator.class)
public @interface RestauranteExistente {
    String message() default "Restaurante não encontrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
