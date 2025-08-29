package com.codegym.demo.repositories;

import com.codegym.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName")
    List<Product> findByCategoryName(String categoryName);

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.name = :tagName")
    List<Product> findByTagName(@Param("tagName") String tagName);

    List<Product> findByDiscountPriceIsNotNull();


}
