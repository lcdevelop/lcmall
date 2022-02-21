package com.lcsays.lcmall.api.models.evertabs;

import com.lcsays.lcmall.db.model.WxEvertabsTab;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 2022/2/17 21:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceEx extends WxEvertabsWorkspace {
    private List<TabEx> tabs;
    private long tabsCount;

    public void copyFrom(WxEvertabsWorkspace workspace) {
        this.setId(workspace.getId());
        this.setName(workspace.getName());
        this.setWxMaUserId(workspace.getWxMaUserId());
        this.setCreateTime(workspace.getCreateTime());
        this.setIsTemp(workspace.getIsTemp());
        this.setCreateTime(workspace.getCreateTime());
    }
}