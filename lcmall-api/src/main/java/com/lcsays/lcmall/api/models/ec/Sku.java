package com.lcsays.lcmall.api.models.ec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 18:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sku implements Serializable {
    private Long id;

    private Product product;
    private String name;
    private String image;
    private String specs;
    private List<String> specList;

//    private List<SkuSpec> specList;
    private Price price;

    public Sku(Long id) {
        this.id = id;
    }

    public List<String> getSpecList() {
        if (null != specs && !"".equals(specs)) {
            return Arrays.asList(specs.split(","));
        } else {
            return null;
        }
    }
}
