package com.hybris.shop.service.impl;

import com.hybris.shop.exceptions.orderExceptions.OrderNotFoundByIdException;
import com.hybris.shop.exceptions.orderItemExceptions.OrderItemNotFoundByIdException;
import com.hybris.shop.exceptions.productExceptions.ProductNotFoundByIdException;
import com.hybris.shop.model.Order;
import com.hybris.shop.model.OrderItem;
import com.hybris.shop.model.Product;
import com.hybris.shop.model.idClasses.OrderItemId;
import com.hybris.shop.repository.OrderItemRepository;
import com.hybris.shop.repository.OrderRepository;
import com.hybris.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    private static final long NOT_EXISTING_ORDER_ID = 0L;
    private static final long NOT_EXISTING_PRODUCT_ID = 0L;
    private static final long PRODUCT_ID = 1L;
    private static final long ORDER_ID = 2L;
    private static final int QUANTITY = 5;
    private static final OrderItemId ORDER_ITEM_ID = new OrderItemId();
    private static final OrderItemId NOT_EXISTING_ORDER_ITEM_ID = new OrderItemId();

    private OrderItem orderItemInDb;
    private OrderItem newOrderItem;
    private OrderItem orderItemWithNotExistingId;
    private Order orderWithNotExistingId;

    @InjectMocks
    private OrderItemService orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        ORDER_ITEM_ID.setOrder(ORDER_ID);
        ORDER_ITEM_ID.setProduct(PRODUCT_ID);

        NOT_EXISTING_ORDER_ITEM_ID.setOrder(NOT_EXISTING_ORDER_ID);
        NOT_EXISTING_ORDER_ITEM_ID.setProduct(NOT_EXISTING_PRODUCT_ID);

        Order order = new Order();
        order.setId(ORDER_ID);

        Product product = new Product();
        product.setId(PRODUCT_ID);

        orderWithNotExistingId = new Order();
        orderWithNotExistingId.setId(NOT_EXISTING_ORDER_ID);

        orderItemInDb = new OrderItem();
        orderItemInDb.setProduct(product);
        orderItemInDb.setOrder(order);

        Product productWithNotExistingId = new Product();
        productWithNotExistingId.setId(NOT_EXISTING_PRODUCT_ID);

        newOrderItem = new OrderItem();
        newOrderItem.setOrder(order);
        newOrderItem.setProduct(product);
        newOrderItem.setQuantity(QUANTITY);

        orderItemWithNotExistingId = new OrderItem();
        orderItemWithNotExistingId.setOrder(orderWithNotExistingId);
        orderItemWithNotExistingId.setProduct(productWithNotExistingId);
    }


    @Test
    void shouldThrowExceptionWhenTrySaveWithNoExistingOrder() {
        //given
        newOrderItem.setOrder(orderWithNotExistingId);

        //when
        when(orderRepository.existsById(anyLong())).thenReturn(false);

        //then
        OrderNotFoundByIdException exception =
                assertThrows(OrderNotFoundByIdException.class, () -> orderItemService.save(newOrderItem));

        assertEquals(String.format("Order with id %s was not found", NOT_EXISTING_ORDER_ID), exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTrySaveWithNoExistingProduct() {
        //given
        newOrderItem.getProduct().setId(NOT_EXISTING_PRODUCT_ID);

        //when
        when(orderRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(false);

        //then
        ProductNotFoundByIdException exception =
                assertThrows(ProductNotFoundByIdException.class, () -> orderItemService.save(newOrderItem));

        assertEquals(String.format("Product with id %s was not found", NOT_EXISTING_PRODUCT_ID),
                exception.getMessage());
    }

    @Test
    void shouldFindAndReturnOrderItemById() {
        //given
        //when
        when(orderItemRepository.findById(ORDER_ITEM_ID)).thenReturn(Optional.of(orderItemInDb));

        OrderItem orderItemById = orderItemService.findById(ORDER_ITEM_ID);

        OrderItemId orderItemId = new OrderItemId();
        orderItemId.setOrder(orderItemById.getOrder().getId());
        orderItemId.setProduct(orderItemById.getProduct().getId());

        //then
        assertEquals(ORDER_ITEM_ID, orderItemId);
    }

    @Test
    void shouldThrowExceptionWhenFindOrderItemByNotExistId() {
        //given
        //when
        when(orderItemRepository.findById(NOT_EXISTING_ORDER_ITEM_ID))
                .thenThrow(new OrderItemNotFoundByIdException(NOT_EXISTING_ORDER_ITEM_ID));

        //then
        OrderItemNotFoundByIdException exception =
                assertThrows(OrderItemNotFoundByIdException.class,
                        () -> orderItemService.findById(NOT_EXISTING_ORDER_ITEM_ID));

        assertEquals(String.format("Order item with id %s was not found", NOT_EXISTING_ORDER_ITEM_ID),
                exception.getMessage());
    }

    @Test
    void shouldUpdateOrderItemQuantity() {
        //given
        //when
        when(orderItemRepository.findById(ORDER_ITEM_ID)).thenReturn(Optional.of(orderItemInDb));
        when(orderItemRepository.save(orderItemInDb)).thenReturn(orderItemInDb);
        when(orderRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.existsById(anyLong())).thenReturn(true);

        OrderItem updatedOrderItem = orderItemService.update(ORDER_ITEM_ID, newOrderItem);

        OrderItemId orderItemId = new OrderItemId();
        orderItemId.setOrder(updatedOrderItem.getOrder().getId());
        orderItemId.setProduct(updatedOrderItem.getProduct().getId());

        //then
        assertEquals(ORDER_ITEM_ID, orderItemId);
        assertEquals(QUANTITY, updatedOrderItem.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenTryUpdateOrderItemWithNotExistId() {
        //given
        //when
        when(orderItemRepository.findById(NOT_EXISTING_ORDER_ITEM_ID))
                .thenThrow(new OrderItemNotFoundByIdException(NOT_EXISTING_ORDER_ITEM_ID));

        //then
        OrderItemNotFoundByIdException exception = assertThrows(OrderItemNotFoundByIdException.class,
                () -> orderItemService.update(NOT_EXISTING_ORDER_ITEM_ID, orderItemWithNotExistingId));

        assertEquals(String.format("Order item with id %s was not found", NOT_EXISTING_ORDER_ITEM_ID),
                exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenExistById() {
        //given
        //when
        when(orderItemRepository.existsById(ORDER_ITEM_ID)).thenReturn(true);

        boolean b = orderItemService.existsById(ORDER_ITEM_ID);

        //then
        assertTrue(b);
    }

    @Test
    void shouldReturnFalseWhenExistById() {
        //given
        //when
        when(orderItemRepository.existsById(NOT_EXISTING_ORDER_ITEM_ID)).thenReturn(false);

        boolean b = orderItemService.existsById(NOT_EXISTING_ORDER_ITEM_ID);

        //then
        assertFalse(b);
    }

    @Test
    void shouldDeleteOrderItemById() {
        //given
        //when
        when(orderItemRepository.existsById(ORDER_ITEM_ID)).thenReturn(true);

        orderItemService.deleteById(ORDER_ITEM_ID);

        //then
        verify(orderItemRepository, times(1)).deleteById(ORDER_ITEM_ID);
        verifyNoMoreInteractions(orderItemRepository);
    }

    @Test
    void shouldThrowExceptionWhenDeleteOrderItemByIdWithNotExistingId() {
        //given
        //when
        when(orderItemRepository.existsById(NOT_EXISTING_ORDER_ITEM_ID)).thenReturn(false);

        //then
        OrderItemNotFoundByIdException exception = assertThrows(OrderItemNotFoundByIdException.class,
                () -> orderItemService.deleteById(NOT_EXISTING_ORDER_ITEM_ID));

        assertEquals(String.format("Order item with id %s was not found", NOT_EXISTING_ORDER_ITEM_ID),
                exception.getMessage());
    }

    @Test
    void shouldFindAndReturnAllOrderItemsInDb() {
        //given
        List<OrderItem> orderItemsInDb = List.of(this.orderItemInDb);
        Iterable<OrderItem> orderItemIterable = orderItemsInDb;

        //when
        when(orderItemRepository.findAll()).thenReturn(orderItemIterable);

        List<OrderItem> orderItems = orderItemService.findAll();

        //then
        assertEquals(orderItemsInDb.size(), orderItems.size());
        for (int i = 0; i < orderItems.size(); i++) {
            assertEquals(orderItemsInDb.get(i), orderItems.get(i));
        }
    }
}