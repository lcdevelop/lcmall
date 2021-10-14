package com.lcsays.gg.models.dbwrapper;

import com.lcsays.lcmall.db.model.EcSku;
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
public class EcSkuEx extends EcSku {
    private Integer priceValue;

    public void copyTo(EcSku ecSku) {
        ecSku.setImage(this.getImage());
        ecSku.setName(this.getName());
        ecSku.setIsDel(this.getIsDel());
        ecSku.setSpecs(this.getSpecs());
        ecSku.setProductId(this.getProductId());
    }
}
