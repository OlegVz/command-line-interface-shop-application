package com.hybris.shop.service;

import com.hybris.shop.model.Product;

import java.util.List;

public interface ProductServiceInterface extends ServiceInterface<Product, Long> {
    Product findByName(String name);

    List<Product> sortProductsByNumberOfOrdersDesc();

    boolean existsByName(String name);
}
