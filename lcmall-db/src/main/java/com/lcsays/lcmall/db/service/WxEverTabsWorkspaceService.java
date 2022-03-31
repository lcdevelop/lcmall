package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxEvertabsTabMapper;
import com.lcsays.lcmall.db.dao.WxEvertabsWorkspaceMapper;
import com.lcsays.lcmall.db.model.WxEvertabsTab;
import com.lcsays.lcmall.db.model.WxEvertabsTabExample;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspace;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspaceExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lichuang
 * @Date: 21-10-9 11:12
 */
@Service
public class WxEverTabsWorkspaceService {

    @Resource
    private WxEvertabsWorkspaceMapper workspaceMapper;

    @Resource
    private WxEvertabsTabMapper tabMapper;

    public boolean addWorkspace(WxEvertabsWorkspace workspace, List<WxEvertabsTab> wxEvertabsTabs) {
        int ret = workspaceMapper.insertSelective(workspace);
        if (ret > 0) {
            if (null != wxEvertabsTabs) {
                for (WxEvertabsTab tab : wxEvertabsTabs) {
                    tab.setWorkspaceId(workspace.getId());
                    tabMapper.insertSelective(tab);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public List<WxEvertabsWorkspace> queryAllWorkspaces(Integer wxMaUserId) {
        WxEvertabsWorkspaceExample example = new WxEvertabsWorkspaceExample();
        example.createCriteria().andWxMaUserIdEqualTo(wxMaUserId).andLogicalDeleted(false);
        return workspaceMapper.selectByExample(example);
    }

    public Map<Integer, Long> workspaceTabsCount(List<Integer> workspaceIdList) {
        Map<Integer, Long> ret = new HashMap<>();
        for (Integer workspaceId: workspaceIdList) {
            WxEvertabsTabExample example = new WxEvertabsTabExample();
            example.createCriteria().andWorkspaceIdEqualTo(workspaceId).andLogicalDeleted(false);
            long count = tabMapper.countByExample(example);
            ret.put(workspaceId, count);
        }
        return ret;
    }

    public WxEvertabsWorkspace queryWorkspace(Integer workspaceId) {
        return workspaceMapper.selectByPrimaryKeyWithLogicalDelete(workspaceId, false);
    }

    public List<WxEvertabsTab> queryAllTabsByWorkspaceId(Integer workspaceId) {
        WxEvertabsTabExample example = new WxEvertabsTabExample();
        example.createCriteria().andWorkspaceIdEqualTo(workspaceId).andLogicalDeleted(false);
        return tabMapper.selectByExample(example);
    }

    public List<WxEvertabsTab> queryAllTabs(List<Integer> workspaceIdList) {
        WxEvertabsTabExample example = new WxEvertabsTabExample();
        example.createCriteria().andWorkspaceIdIn(workspaceIdList);
        return tabMapper.selectByExample(example);
    }

    public WxEvertabsWorkspace queryWorkspaceById(Integer workspaceId) {
        return workspaceMapper.selectByPrimaryKey(workspaceId);
    }

    public int update(WxEvertabsWorkspace workspace) {
        return workspaceMapper.updateByPrimaryKey(workspace);
    }

    public int delete(WxEvertabsWorkspace workspace) {
        return workspaceMapper.logicalDeleteByPrimaryKey(workspace.getId());
    }

    public int updateTab(WxEvertabsTab tab) {
        return tabMapper.updateByPrimaryKeySelective(tab);
    }

    public int createTab(WxEvertabsTab tab) {
        return tabMapper.insertSelective(tab);
    }

    public void replaceTabs(Integer workspaceId, List<WxEvertabsTab> newTabs) {
        WxEvertabsTabExample example = new WxEvertabsTabExample();
        example.createCriteria().andWorkspaceIdEqualTo(workspaceId).andLogicalDeleted(false);
        List<WxEvertabsTab> oldTabs = tabMapper.selectByExample(example);
        int index = 0;
        for (WxEvertabsTab tab: oldTabs) {
            if (newTabs.size() > index) {
                WxEvertabsTab newTab = newTabs.get(index);
                boolean needUpdate = false;
                if (!newTab.getId().equals(tab.getId())) {
                    tab.setId(newTab.getId());
                    needUpdate = true;
                }
                if ((null == newTab.getFavIconUrl() && null != tab.getFavIconUrl())
                        || (null != newTab.getFavIconUrl() && !newTab.getFavIconUrl().equals(tab.getFavIconUrl()))) {
                    tab.setFavIconUrl(newTab.getFavIconUrl());
                    needUpdate = true;
                }
                if (!newTab.getTitle().equals(tab.getTitle())) {
                    tab.setTitle(newTab.getTitle());
                    needUpdate = true;
                }
                if (!newTab.getUrl().equals(tab.getUrl())) {
                    tab.setUrl(newTab.getUrl());
                    needUpdate = true;
                }
                if (needUpdate) {
                    tabMapper.updateByPrimaryKeySelective(tab);
                }
            } else {
                tabMapper.logicalDeleteByPrimaryKey(tab.getPkId());
            }
            index++;
        }

        while (newTabs.size() > index) {
            WxEvertabsTab newTab = newTabs.get(index);
            newTab.setWorkspaceId(workspaceId);
            tabMapper.insertSelective(newTab);
            index++;
        }
    }

    public Map<Integer, List<WxEvertabsTab>> queryWorkspaceTabsMap(List<Integer> workspaceIdList) {
        Map<Integer, List<WxEvertabsTab>> ret = new HashMap<>();
        if (workspaceIdList.size() > 0) {
            WxEvertabsTabExample example = new WxEvertabsTabExample();
            example.createCriteria().andWorkspaceIdIn(workspaceIdList).andLogicalDeleted(false);
            List<WxEvertabsTab> tabs = tabMapper.selectByExample(example);
            for (WxEvertabsTab tab : tabs) {
                ret.computeIfAbsent(tab.getWorkspaceId(), t -> new ArrayList<>()).add(tab);
            }
        }
        return ret;
    }
}
