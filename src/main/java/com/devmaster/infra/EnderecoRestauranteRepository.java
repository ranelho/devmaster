package com.devmaster.infra;

import com.devmaster.domain.EnderecoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRestauranteRepository extends JpaRepository<EnderecoRestaurante, Long> {

    List<EnderecoRestaurante> findAllRestauranteId(Long restauranteId);

}
