package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewProductDto;
import com.hybris.shop.dto.ProductDto;
import com.hybris.shop.model.Product;

public interface ProductMapperInterface {
    Product fromNewProductDtoToEntity(NewProductDto newProductDto);

    ProductDto fromEntityToProductDto(Product product);
}
