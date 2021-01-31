package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductWithSuchNameExistException;
import com.hybris.shop.exceptions.productExceptions.ProductWithSuchNameNotExistException;
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
        String productName = product.getName();
        if (productRepository.existsByName(productName)) {
            throw new ProductWithSuchNameExistException(productName);
        }

        return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundByIdException(id));
    }

    @Override
    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundByIdException(id);
        }
    }
}
