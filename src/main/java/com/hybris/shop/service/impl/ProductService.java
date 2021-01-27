package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.ProductNotFoundByIdException;
import com.hybris.shop.exceptions.ProductWithSuchNameNotExistException;
import com.hybris.shop.model.Product;
import com.hybris.shop.repository.ProductRepository;
import com.hybris.shop.service.ProductServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new ProductWithSuchNameNotExistException(productName));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundByIdException(id));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
