package com.hybris.shop.service.impl.integration;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.model.Product;
import com.hybris.shop.service.impl.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ShopApplication.class})
public class ProductServiceTestIT {

    private static final String PRODUCT_NAME = "Product name";
    private static final int PRICE = 123;
    private static final Product.ProductStatus PRODUCT_STATUS = Product.ProductStatus.IN_STOCK;
    private static final LocalDateTime DATE_TIME =
            LocalDateTime.of(2021, 1, 30, 12, 56);

    private Product product;

    @Autowired
    public ProductService productService;

    @BeforeEach
    public void init() {
        product = new Product();
        product.setName(PRODUCT_NAME);
        product.setPrice(PRICE);
        product.setStatus(PRODUCT_STATUS);
        product.setCreatedAt(DATE_TIME);
    }

    @Test
    void shouldSaveNewProduct() {
        //given
        //when
        Product savedProduct = productService.save(product);

        //then
        assertNotNull(savedProduct.getId());
        assertEquals(PRODUCT_NAME, savedProduct.getName());
        assertEquals(PRICE, savedProduct.getPrice());
        assertEquals(PRODUCT_STATUS, savedProduct.getStatus());
        assertEquals(DATE_TIME, savedProduct.getCreatedAt());
    }
}
