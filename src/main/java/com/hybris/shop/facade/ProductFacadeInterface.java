package com.hybris.shop.facade;

import com.hybris.shop.dto.NewProductDto;
import com.hybris.shop.dto.ProductDto;

import java.util.List;

public interface ProductFacadeInterface extends FacadeInterface<ProductDto, NewProductDto, Long>{
    boolean existsByName(String name);

    List<ProductDto> sortProductsByNumberOfOrdersDesc();

    void deleteAll();
}
