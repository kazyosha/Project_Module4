package com.codegym.demo.repositories;

import com.codegym.demo.models.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductTagRepository extends JpaRepository<ProductTag, Long> {
}
