package com.diegosimon.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.diegosimon.crud.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
