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
public class Cart implements Serializable {
    private Long id;

    private Long wxMaUserId;
    private Sku sku;
    private Integer count;
}
