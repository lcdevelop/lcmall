package com.lcsays.lcmall.api.models.dbwrapper;

import com.lcsays.lcmall.db.model.EcCategory;
import com.lcsays.lcmall.db.model.EcProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lichuang
 * @Date: 21-9-30 11:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcProductEx extends EcProduct {
    private EcCategory category;

    public void copyFrom(EcProduct product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setCategoryId(product.getCategoryId());
        this.setDescription(product.getDescription());
        this.setCreateTime(product.getCreateTime());
        this.setIsDel(product.getIsDel());
        this.setUpdateTime(product.getUpdateTime());
    }
}
