package com.hybris.shop.repository;

import com.hybris.shop.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);

    boolean existsByName(String name);

    @Query(value = "SELECT a.id, a.created_at, a.name, a.price, a.status\n" +
            "FROM (SELECT DISTINCT id, created_at, name, price, status,  SUM(quantity) AS ordered\n" +
            "      FROM products\n" +
            "               LEFT JOIN order_item oi ON products.id = oi.product_id\n" +
            "      GROUP BY id\n" +
            "      HAVING ordered > 0\n" +
            "      ORDER BY ordered DESC) AS a",
            nativeQuery = true)
    List<Product> sortProductsByNumberOfOrdersDesc();
}
