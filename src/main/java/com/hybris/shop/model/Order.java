package com.hybris.shop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(of = {"id", "user", "status", "createdAt"})
@ToString(of = {"id", "user", "status", "createdAt"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private String createdAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    List<OrderItem> orderItems;

    public enum OrderStatus{
        NEW_ORDER("New order"),
        CONFIRMED_ORDER("Confirmed order"),
        FULFILLED_ORDER("Ful filled order");

        String status;
        OrderStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
