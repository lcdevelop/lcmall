package com.lcsays.gg.models.ec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: lichuang
 * @Date: 21-8-11 18:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAffiliate implements Serializable {
    private Long id;
    private Order order;
    private Sku sku;
    private Integer count;
    private Integer price;

    public OrderAffiliate(Order order, Sku sku, Integer count, Integer price) {
        this.order = order;
        this.sku = sku;
        this.count = count;
        this.price = price;
    }
}
