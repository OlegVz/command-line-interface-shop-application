package com.hybris.shop.model;

import com.hybris.shop.model.idClasses.OrderItemId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@IdClass(OrderItemId.class)
@Table(name = "order_item")
@EqualsAndHashCode(of = {"order", "product", "quantity"})
@ToString(of = {"order", "product", "quantity"})
public class OrderItem {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;
}
