package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.ProductNotFoundByIdException;
import com.hybris.shop.exceptions.ProductWithSuchNameExistException;
import com.hybris.shop.exceptions.ProductWithSuchNameNotExistException;
import com.hybris.shop.model.Product;
import com.hybris.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final long PRODUCT_ID = 1L;
    private static final String PRODUCT_NAME = "Product name";
    private static final int PRICE = 123;
    private static final Product.ProductStatus PRODUCT_STATUS = Product.ProductStatus.IN_STOCK;
    private static final LocalDateTime DATE_TIME =
            LocalDateTime.of(2021, 1, 30, 12, 56);
    public static final long NOT_EXIST_ID = 0L;
    public static final String NEW_PRODUCT_NAME = "New product name";
    public static final int NEW_PRICE = 456;
    public static final Product.ProductStatus NEW_PRODUCT_STATUS = Product.ProductStatus.RUNNING_LOW;
    public static final LocalDateTime NEW_DATE_TIME =
            LocalDateTime.of(2022, 2, 15, 14, 48);

    private Product productInDB;
    private Product newProduct;

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productInDB = new Product();
        productInDB.setId(PRODUCT_ID);
        productInDB.setName(PRODUCT_NAME);
        productInDB.setPrice(PRICE);
        productInDB.setStatus(PRODUCT_STATUS);
        productInDB.setCreatedAt(DATE_TIME);

        newProduct = new Product();
        newProduct.setName(NEW_PRODUCT_NAME);
        newProduct.setPrice(NEW_PRICE);
        newProduct.setStatus(NEW_PRODUCT_STATUS);
        newProduct.setCreatedAt(NEW_DATE_TIME);
    }

    @Test
    void shouldThrowExceptionWhenTrySaveProductWithExistingName() {
        //given
        //when
        when(productRepository.existsByName(anyString())).thenReturn(true);

        //then
        ProductWithSuchNameExistException exception =
                assertThrows(ProductWithSuchNameExistException.class, () -> productService.save(newProduct));
        assertEquals(String.format("Product with such name exist: %s", NEW_PRODUCT_NAME), exception.getMessage());
    }

    @Test
    void shouldFindAndReturnProductByName() {
        //given
        //when
        when(productRepository.findByName(anyString())).thenReturn(Optional.of(productInDB));

        Product product = productService.findByName(PRODUCT_NAME);

        //then
        assertEquals(PRODUCT_NAME, product.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductWithSuchNameNotExist() {
        //given
        //when
        when(productRepository.findByName(anyString()))
                .thenThrow(new ProductWithSuchNameNotExistException(PRODUCT_NAME));

        //then
        ProductWithSuchNameNotExistException exception =
                assertThrows(ProductWithSuchNameNotExistException.class, () -> productService.findByName(PRODUCT_NAME));

        assertEquals(String.format("Product with such name not exist: %s", PRODUCT_NAME), exception.getMessage());
    }

    @Test
    void shouldFindAndReturnProductById() {
        //given
        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productInDB));

        Product byId = productService.findById(PRODUCT_ID);

        //then
        assertEquals(PRODUCT_ID, byId.getId());
    }

    @Test
    void shouldThrowExceptionWhenProductWithSuchIdNotExist() {
        //given
        //when
        when(productRepository.findById(anyLong())).thenThrow(new ProductNotFoundByIdException(NOT_EXIST_ID));

        //then
        ProductNotFoundByIdException exception =
                assertThrows(ProductNotFoundByIdException.class, () -> productService.findById(NOT_EXIST_ID));

        assertEquals(String.format("Product with id %s was not found", NOT_EXIST_ID), exception.getMessage());

    }

    @Test
    void shouldUpdateProductData() {
        //given
        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productInDB));
        when(productService.save(productInDB)).thenReturn(productInDB);

        Product updatedProduct = productService.update(PRODUCT_ID, newProduct);

        //then
        assertEquals(PRODUCT_ID, updatedProduct.getId());
        assertEquals(NEW_PRODUCT_NAME, updatedProduct.getName());
        assertEquals(NEW_PRICE, updatedProduct.getPrice());
        assertEquals(NEW_PRODUCT_STATUS, updatedProduct.getStatus());
        assertEquals(NEW_DATE_TIME, updatedProduct.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyProductName() {
        //given
        newProduct.setPrice(null);
        newProduct.setStatus(null);
        newProduct.setCreatedAt(null);

        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productInDB));
        when(productService.save(productInDB)).thenReturn(productInDB);

        Product updatedProduct = productService.update(PRODUCT_ID, newProduct);

        //then
        assertEquals(PRODUCT_ID, updatedProduct.getId());
        assertEquals(NEW_PRODUCT_NAME, updatedProduct.getName());
        assertEquals(PRICE, updatedProduct.getPrice());
        assertEquals(PRODUCT_STATUS, updatedProduct.getStatus());
        assertEquals(DATE_TIME, updatedProduct.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyProductPrice() {
        //given
        newProduct.setName(null);
        newProduct.setStatus(null);
        newProduct.setCreatedAt(null);

        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productInDB));
        when(productService.save(productInDB)).thenReturn(productInDB);

        Product updatedProduct = productService.update(PRODUCT_ID, newProduct);

        //then
        assertEquals(PRODUCT_ID, updatedProduct.getId());
        assertEquals(PRODUCT_NAME, updatedProduct.getName());
        assertEquals(NEW_PRICE, updatedProduct.getPrice());
        assertEquals(PRODUCT_STATUS, updatedProduct.getStatus());
        assertEquals(DATE_TIME, updatedProduct.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyProductStatus() {
        //given
        newProduct.setName(null);
        newProduct.setPrice(null);
        newProduct.setCreatedAt(null);

        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productInDB));
        when(productService.save(productInDB)).thenReturn(productInDB);

        Product updatedProduct = productService.update(PRODUCT_ID, newProduct);

        //then
        assertEquals(PRODUCT_ID, updatedProduct.getId());
        assertEquals(PRODUCT_NAME, updatedProduct.getName());
        assertEquals(PRICE, updatedProduct.getPrice());
        assertEquals(NEW_PRODUCT_STATUS, updatedProduct.getStatus());
        assertEquals(DATE_TIME, updatedProduct.getCreatedAt());
    }

    @Test
    void shouldUpdateOnlyProductCreationDate() {
        //given
        newProduct.setName(null);
        newProduct.setPrice(null);
        newProduct.setStatus(null);

        //when
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productInDB));
        when(productService.save(productInDB)).thenReturn(productInDB);

        Product updatedProduct = productService.update(PRODUCT_ID, newProduct);

        //then
        assertEquals(PRODUCT_ID, updatedProduct.getId());
        assertEquals(PRODUCT_NAME, updatedProduct.getName());
        assertEquals(PRICE, updatedProduct.getPrice());
        assertEquals(PRODUCT_STATUS, updatedProduct.getStatus());
        assertEquals(NEW_DATE_TIME, updatedProduct.getCreatedAt());
    }

    @Test
    void shouldDeleteProductById() {
        //given
        //when
        when(productRepository.existsById(anyLong())).thenReturn(true);
        productService.deleteById(PRODUCT_ID);

        //then
        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldThrowExceptionWhenDeleteProductByIdWithNotExistingId() {
        //given
        //when
        when(productRepository.existsById(anyLong())).thenReturn(false);

        //then
        ProductNotFoundByIdException exception =
                assertThrows(ProductNotFoundByIdException.class, () -> productService.deleteById(NOT_EXIST_ID));
        assertEquals(String.format("Product with id %s was not found", NOT_EXIST_ID), exception.getMessage());
    }
    }