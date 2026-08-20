package com.devmaster.handler.validator;


import com.devmaster.infra.RestauranteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestauranteExistenteValidator implements ConstraintValidator<RestauranteExistente, Long> {

    private final RestauranteRepository restauranteRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return id !=  null && restauranteRepository.existsById(id);
    }
}
