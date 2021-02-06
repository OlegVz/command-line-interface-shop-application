package com.hybris.shop.service.impl.integration;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.Product;
import com.hybris.shop.repository.OrderItemRepository;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.service.impl.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShopApplication.class})
public class ProductServiceTestIT {

    private static final String PRODUCT_NAME = "Product name";
    private static final int PRICE = 123;
    private static final Product.ProductStatus PRODUCT_STATUS = Product.ProductStatus.IN_STOCK;
    private static final LocalDateTime DATE_TIME =
            LocalDateTime.of(2021, 1, 30, 12, 56);

    private Product product;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    @Test
    void shouldReturnListOfProductsSortedByNumberOfOrdersDesc() {
        //given
        Product product1 = new Product();
        product1.setName("name1");
        Long productId1 = productService.save(product1).getId();

        Product product2 = new Product();
        product2.setName("name2");
        Long productId2 = productService.save(product2).getId();

        Product product3 = new Product();
        product3.setName("name3");
        Long productId3 = productService.save(product3).getId();

        Long orderId1 = orderRepository.save(new Order()).getId();
        Long orderId2 = orderRepository.save(new Order()).getId();
        Long orderId3 = orderRepository.save(new Order()).getId();
        Long orderId4 = orderRepository.save(new Order()).getId();

        OrderItem orderItem1 = new OrderItem();
        orderRepository.findById(orderId1).ifPresent(orderItem1::setOrder);
        orderItem1.setProduct(productService.findById(productId1));
        int orderItemQuantity1 = 5;
        orderItem1.setQuantity(orderItemQuantity1);

        OrderItem orderItem2 = new OrderItem();
        orderRepository.findById(orderId2).ifPresent(orderItem2::setOrder);
        orderItem2.setProduct(productService.findById(productId2));
        int orderItemQuantity2 = 1;
        orderItem2.setQuantity(orderItemQuantity2);

        OrderItem orderItem3 = new OrderItem();
        orderRepository.findById(orderId3).ifPresent(orderItem3::setOrder);
        orderItem3.setProduct(productService.findById(productId3));
        int orderItemQuantity3 = 10;
        orderItem3.setQuantity(orderItemQuantity3);

        OrderItem orderItem4 = new OrderItem();
        orderRepository.findById(orderId4).ifPresent(orderItem4::setOrder);
        orderItem4.setProduct(productService.findById(productId1));
        int orderItemQuantity4 = 15;
        orderItem4.setQuantity(orderItemQuantity4);

        List.of(orderItem1, orderItem2, orderItem3, orderItem4)
                .forEach(orderItem -> orderItemRepository.save(orderItem));

        //when
        List<Product> products = productService.sortProductsByNumberOfOrdersDesc();
        int quantityOfOrderedProducts = 3;

        //then
        assertEquals(quantityOfOrderedProducts, products.size());
        assertEquals(productId1, products.get(0).getId());
        assertEquals(productId3, products.get(1).getId());
        assertEquals(productId2, products.get(2).getId());
    }

    @Test
    void shouldReturnEmptyListOfProductsSortedByNumberOfOrdersDesc() {
        //given
        //when
        List<Product> products = productService.sortProductsByNumberOfOrdersDesc();

        //then
        assertTrue(products.isEmpty());
    }


}
