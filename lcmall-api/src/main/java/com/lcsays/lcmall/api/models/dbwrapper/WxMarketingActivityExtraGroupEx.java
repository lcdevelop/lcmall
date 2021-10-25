package com.lcsays.lcmall.api.models.dbwrapper;

import com.lcsays.lcmall.db.model.WxMarketingActivityExtra;
import com.lcsays.lcmall.db.model.WxMarketingActivityExtraGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-10-22 12:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxMarketingActivityExtraGroupEx extends WxMarketingActivityExtraGroup {

    private List<WxMarketingActivityExtra> extras;

    public void copyFrom(WxMarketingActivityExtraGroup group) {
        this.setId(group.getId());
        this.setName(group.getName());
    }
}
