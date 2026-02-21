package com.devmaster.infrastructure.repository;

import com.devmaster.domain.Cupom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para Cupom.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {
    
    Optional<Cupom> findByCodigo(String codigo);
    
    boolean existsByCodigo(String codigo);
    
    Page<Cupom> findByAtivo(Boolean ativo, Pageable pageable);
    
    @Query("SELECT c FROM Cupom c WHERE c.ativo = true AND c.validoDe <= :agora AND c.validoAte >= :agora")
    List<Cupom> findCuponsValidos(@Param("agora") LocalDateTime agora);
    
    @Query("SELECT c FROM Cupom c WHERE c.ativo = true AND c.validoDe <= :agora AND c.validoAte >= :agora")
    Page<Cupom> findCuponsValidosComPaginacao(@Param("agora") LocalDateTime agora, Pageable pageable);
    
    @Query("SELECT c FROM Cupom c WHERE c.validoAte < :agora")
    List<Cupom> findCuponsExpirados(@Param("agora") LocalDateTime agora);
    
    @Query("SELECT c FROM Cupom c WHERE c.limiteUso IS NOT NULL AND c.quantidadeUsada >= c.limiteUso")
    List<Cupom> findCuponsComLimiteAtingido();
}
