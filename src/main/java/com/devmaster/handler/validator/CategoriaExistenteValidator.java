package com.devmaster.handler.validator;

import com.devmaster.infra.CategoriaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CategoriaExistenteValidator implements ConstraintValidator<CategoriaExistente, Long> {

    private final CategoriaRepository categoriaRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return id != null && categoriaRepository.existsById(id);
    }
}
