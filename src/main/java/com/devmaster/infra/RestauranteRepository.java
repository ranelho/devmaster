package com.devmaster.infra;

import com.devmaster.domain.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    Boolean existsByCnpj(String cnpj);
    Boolean existsBySlug(String slug);
}