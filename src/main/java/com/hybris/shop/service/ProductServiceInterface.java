package com.hybris.shop.service;

import com.hybris.shop.model.Product;

public interface ProductServiceInterface extends ServiceInterface<Product> {
    Product findByName(String name);
}
