package com.devmaster.application.repository;

import com.devmaster.domain.Desconto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DescontoRepository extends JpaRepository<Desconto, Long> {
    
    List<Desconto> findByProdutoId(Long produtoId);
    
    @Query("SELECT d FROM Desconto d WHERE d.produto.id = :produtoId " +
           "AND d.ativo = true " +
           "AND :dataHora BETWEEN d.dataHoraInicio AND d.dataHoraFim")
    Optional<Desconto> findDescontoVigentePorProduto(
        @Param("produtoId") Long produtoId, 
        @Param("dataHora") LocalDateTime dataHora
    );
    
    @Query("SELECT d FROM Desconto d WHERE d.ativo = true " +
           "AND :dataHora BETWEEN d.dataHoraInicio AND d.dataHoraFim")
    List<Desconto> findDescontosVigentes(@Param("dataHora") LocalDateTime dataHora);
}
