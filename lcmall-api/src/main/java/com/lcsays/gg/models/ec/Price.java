package com.lcsays.gg.models.ec;

import com.lcsays.gg.enums.ec.PricePolicy;
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
public class Price implements Serializable {
    private Long id;

    private Long skuId;
    private int policy;

    private int price;

    private int computePrice() {
        if (policy == PricePolicy.PP_RAW.getValue()) {
            return price;
        } else {
            return -1;
        }
    }

    public int getPrice() {
        return this.computePrice();
    }
}
