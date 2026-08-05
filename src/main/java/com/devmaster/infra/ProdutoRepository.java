package com.devmaster.infra;

import com.devmaster.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findAllByCategoriaIdOrderByNomeAsc(Long id, Pageable pageable);

}
