package com.hybris.shop.service.impl.integration;

import com.hybris.shop.ShopApplication;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.Product;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.ProductRepository;
import com.hybris.shop.service.impl.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ShopApplication.class})
public class OrderItemServiceTestIT {

    private Order orderInDb;
    private Product productInDb;
    private OrderItem orderItem;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    public void init() {
        orderInDb = orderRepository.save(new Order());
        productInDb = productRepository.save(new Product());

        orderItem = new OrderItem();
        orderItem.setOrder(orderInDb);
        orderItem.setProduct(productInDb);
    }

    @Test
    void shouldSaveNewOrderItem() {
        //given
        //when
        OrderItem savedOrderItem = orderItemService.save(orderItem);

        //then
        assertEquals(orderInDb, savedOrderItem.getOrder());
        assertEquals(productInDb, savedOrderItem.getProduct());
        assertEquals(orderItem.getQuantity(), savedOrderItem.getQuantity());
    }
}
