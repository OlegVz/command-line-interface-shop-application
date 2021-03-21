package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductWithSuchNameExistException;
import com.hybris.shop.exceptions.productExceptions.ProductWithSuchNameNotExistException;
import com.hybris.shop.model.Product;
import com.hybris.shop.repository.OrderItemRepository;
import com.hybris.shop.repository.ProductRepository;
import com.hybris.shop.service.ProductServiceInterface;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//@Service
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

//    @Autowired
    public ProductService(ProductRepository productRepository,
                          OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
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
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundByIdException(id);
        }
    }

    @Override
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> sortProductsByNumberOfOrdersDesc() {
        return productRepository.sortProductsByNumberOfOrdersDesc();
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }
}
