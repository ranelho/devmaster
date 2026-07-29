package com.devmaster.infra;

import com.devmaster.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query(name = "Buscar Ativos", value = "SELECT * FROM Categoria c where c.restaurante.id = ? and c.ativo = true ORDER BY c.ordemExibicao ")
    List<Categoria> findAllByRestauranteId(Long id);

}
