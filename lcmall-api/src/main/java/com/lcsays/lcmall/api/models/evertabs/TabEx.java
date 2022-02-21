package com.lcsays.lcmall.api.models.evertabs;

import com.lcsays.lcmall.db.model.EcProduct;
import com.lcsays.lcmall.db.model.WxEvertabsTab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 2022/2/17 21:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabEx extends WxEvertabsTab {
    private Integer localId;

    public void copyTo(WxEvertabsTab tab) {
        tab.setWorkspaceId(this.getWorkspaceId());
        tab.setCreateTime(this.getCreateTime());
        tab.setFavIconUrl(this.getFavIconUrl());
        tab.setOrder(this.getOrder());
        tab.setTitle(this.getTitle());
        tab.setUrl(this.getUrl());
    }

    public static List<WxEvertabsTab> toEverTabsTabList(List<TabEx> tabExes) {
        List<WxEvertabsTab> tabs = new ArrayList<>();
        for (TabEx tabEx: tabExes) {
            WxEvertabsTab tab = new WxEvertabsTab();
            tabEx.copyTo(tab);
            tab.setLocalId(tabEx.getId());
            tabs.add(tab);
        }
        return tabs;
    }

    @Override
    public String toString() {
        return "TabEx{" +
                "localId=" + localId +
                "Tab=" + super.toString() +
                '}';
    }
}