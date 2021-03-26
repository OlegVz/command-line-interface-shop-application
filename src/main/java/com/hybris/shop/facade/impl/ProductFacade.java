package com.hybris.shop.facade.impl;

import com.hybris.shop.dto.NewProductDto;
import com.hybris.shop.dto.ProductDto;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.facade.ProductFacadeInterface;
import com.hybris.shop.mapper.ProductMapperInterface;
import com.hybris.shop.model.Product;
import com.hybris.shop.service.ProductServiceInterface;

import java.util.List;
import java.util.stream.Collectors;

//@Component
public class ProductFacade implements ProductFacadeInterface {

    private final ProductServiceInterface productService;

    private final ProductMapperInterface productMapperInterface;

//    @Autowired
    public ProductFacade(ProductServiceInterface productService,
                         ProductMapperInterface productMapperInterface) {
        this.productService = productService;
        this.productMapperInterface = productMapperInterface;
    }

    @Override
    public ProductDto save(NewProductDto newProductDto) {
        Product savedProduct = productService.save(productMapperInterface.fromNewProductDtoToEntity(newProductDto));

        return productMapperInterface.fromEntityToProductDto(savedProduct);
    }

    @Override
    public ProductDto findById(Long id) {
        Product productById = productService.findById(id);

        return productMapperInterface.fromEntityToProductDto(productById);
    }

    @Override
    public ProductDto update(Long id, NewProductDto newProductDto) {
        if (!productService.existsById(id)) {
            throw new ProductNotFoundByIdException(id);
        }

        Product updatedProduct = productService.update(id, productMapperInterface.fromNewProductDtoToEntity(newProductDto));

        return productMapperInterface.fromEntityToProductDto(updatedProduct);
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
                .map(productMapperInterface::fromEntityToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return productService.existsByName(name);
    }

    @Override
    public List<ProductDto> sortProductsByNumberOfOrdersDesc() {
        List<Product> products = productService.sortProductsByNumberOfOrdersDesc();

        return products.stream()
                .map(productMapperInterface::fromEntityToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        productService.deleteAll();
    }
}
