package com.devmaster.handler.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoriaExistenteValidator.class)
public @interface CategoriaExistente {
    String message() default "Categoria não encontrada";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
