package com.hybris.shop.model.idClasses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"order", "product"})
@ToString(of = {"order", "product"})
public class OrderItemId implements Serializable {

    private Long order;
    private Long product;
}
