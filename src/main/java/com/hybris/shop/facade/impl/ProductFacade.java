package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewProductDto;
import com.hybris.shop.dto.ProductDto;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.facade.ProductFacadeInterface;
import com.hybris.shop.mapper.ProductMapper;
import com.hybris.shop.model.Product;
import com.hybris.shop.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductFacade implements ProductFacadeInterface {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @Autowired
    public ProductFacade(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto save(NewProductDto newProductDto) {
        Product savedProduct = productService.save(productMapper.fromNewProductDtoToEntity(newProductDto));

        return productMapper.fromEntityToProductDto(savedProduct);
    }

    @Override
    public ProductDto findById(Long id) {
        Product productById = productService.findById(id);

        return productMapper.fromEntityToProductDto(productById);
    }

    @Override
    public ProductDto update(Long id, NewProductDto newProductDto) {
        if (!productService.existsById(id)) {
            throw new ProductNotFoundByIdException(id);
        }

        Product updatedProduct = productService.update(id, productMapper.fromNewProductDtoToEntity(newProductDto));

        return productMapper.fromEntityToProductDto(updatedProduct);
    }

    @Override
    public boolean existsById(Long id) {
        return productService.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        productService.deleteById(id);
    }

    @Override
    public List<ProductDto> findAll() {
        return productService.findAll().stream()
                .map(productMapper::fromEntityToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return productService.existsByName(name);
    }
}
