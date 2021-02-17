package com.hybris.shop.facade.impl;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.Product;
import com.hybris.shop.repository.OrderItemRepository;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.ProductRepository;
import com.hybris.shop.service.impl.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ShopApplication.class})
class ProductFacadeTestIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void DB() {
        productRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();

    }

    @Test
    void shouldSortProductsByNumberOfOrdersDesc() {
        //given
        ArrayList<Order> orders = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            orders.add(orderRepository.save(new Order()));
        }

        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            products.add(productRepository.save(new Product()));
        }

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder(orders.get(0));
        orderItem1.setProduct(products.get(0));
        orderItem1.setQuantity(3);

        final OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(orders.get(1));
        orderItem2.setProduct(products.get(1));
        orderItem2.setQuantity(1);

        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);

        //when
        List<Product> orderedProducts = productService.sortProductsByNumberOfOrdersDesc();

        //then
        assertEquals(2, orderedProducts.size());
        assertEquals(products.get(0), orderedProducts.get(0));
        assertEquals(products.get(1), orderedProducts.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenSortProductsByNumberOfOrdersDesc() {
        //given
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            products.add(productRepository.save(new Product()));
        }

        //when
        List<Product> orderedProducts = productService.sortProductsByNumberOfOrdersDesc();

        //then
        assertTrue(orderedProducts.isEmpty());
    }
}