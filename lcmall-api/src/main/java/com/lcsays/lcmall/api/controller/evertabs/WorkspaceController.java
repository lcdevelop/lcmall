package com.lcsays.lcmall.api.controller.evertabs;

import com.lcsays.lcmall.api.config.logger.UserIdLogConverter;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.evertabs.WorkspaceEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.ColorUtils;
import com.lcsays.lcmall.api.utils.CookieTokenUtils;
import com.lcsays.lcmall.db.model.WxEvertabsFeedback;
import com.lcsays.lcmall.db.model.WxEvertabsTab;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspace;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxEverTabsFeedbackService;
import com.lcsays.lcmall.db.service.WxEverTabsWorkspaceService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lcsays.lcmall.api.constant.Names.REDIS_FAVICON_KEY;

/**
 * @Author: lichuang
 * @Date: 2022/2/17 21:03
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/evertabs/workspace")
public class WorkspaceController {

    @Resource
    WxEverTabsWorkspaceService everTabsWorkspaceService;

    @Resource
    WxEverTabsFeedbackService feedbackService;

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    private WxMaUser check(HttpServletRequest request) {
        String tokenValue = CookieTokenUtils.getTokenValue(request);
        if (null == tokenValue) {
            return null;
        }

        WxMaUser wxMaUser = wxMaUserService.queryUsersByToken(tokenValue);
        if (null != wxMaUser) {
            UserIdLogConverter.LoggerConfigHolder.set(String.valueOf(wxMaUser.getId()));
        }
        return wxMaUser;
    }

    @PostMapping("/addWorkspace")
    public BaseModel<WorkspaceEx> addWorkspace(HttpServletRequest request,
                                          @RequestBody WorkspaceEx workspaceEx) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        workspaceEx.setWxMaUserId(wxMaUser.getId());
        workspaceEx.setColor(ColorUtils.randomColor());

        if (everTabsWorkspaceService.addWorkspace(workspaceEx, workspaceEx.getTabs())) {
            log.info("addWorkspace success {}", workspaceEx);
            return BaseModel.success(workspaceEx);
        } else {
            log.error("addWorkspace fail {}", workspaceEx);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }

    @GetMapping("/workspaceList")
    public BaseModel<List<WorkspaceEx>> workspaceList(HttpServletRequest request) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        return BaseModel.success(getCurWorkspaceList(wxMaUser.getId()));
    }

    @GetMapping("/tabList")
    public BaseModel<List<WxEvertabsTab>> tabList(HttpServletRequest request, @RequestParam Integer workspaceId) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        if (null == everTabsWorkspaceService.queryWorkspace(workspaceId)) {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }

        List<WxEvertabsTab> tabs = everTabsWorkspaceService.queryAllTabsByWorkspaceId(workspaceId);
        log.info("tabList workspaceId={} tabs.size={}", workspaceId, tabs.size());
        return BaseModel.success(tabs);
    }

    private BaseModel<WxEvertabsTab> internalUpdateTabByPriKey(WxEvertabsTab tab) {
        tab.setWorkspaceId(null); // 如果明确指定tab，则workspaceId不更新，避免更新错了
        if (everTabsWorkspaceService.updateTab(tab) > 0) {
            log.info("updateTab success {}", tab);
            // 这里重新查一下，补充全信息
            return BaseModel.success(everTabsWorkspaceService.queryTabByPkId(tab.getPkId()));
        } else {
            log.error("updateTab fail {}", tab);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }

    /**
     * 生成指定工作区里tab的顺序值
     * @param newTab 新增的带sort值的tab
     */
    private void genTabsSortValue(Integer workspaceId, WxEvertabsTab newTab) {
        if (null == newTab.getSort() || newTab.getSort().equals(0)) {
            return;
        }

        List<WxEvertabsTab> tabs = everTabsWorkspaceService.queryAllTabsByWorkspaceId(workspaceId);
        if (null == tabs || tabs.size() == 0) {
            return;
        }

        // 重新排序，其中略过newTab
        int sort = 1;
        for (WxEvertabsTab tab: tabs) {
            if (!tab.getPkId().equals(newTab.getPkId())) {
                int oldSort = tab.getSort();
                if (sort < newTab.getSort()) {
                    tab.setSort(sort);
                } else {
                    tab.setSort(sort + 1);
                }
                if (oldSort != tab.getSort()) {
                    everTabsWorkspaceService.updateTab(tab);
                }
                sort++;
            }
        }
    }

    @PostMapping("/updateTab")
    public BaseModel<WxEvertabsTab> updateTab(HttpServletRequest request,
                                               @RequestBody WxEvertabsTab tab) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        // 加入待抓取队列
        if (null != tab.getFavIconUrl()) {
            Object value = redisTemplate.opsForValue().get(tab.getFavIconUrl());
            if (null == value) {
                redisTemplate.opsForList().leftPush(REDIS_FAVICON_KEY, tab.getFavIconUrl());
            } else {
                tab.setFavIconUrl(String.valueOf(value));
            }
        }

        if (null != tab.getPkId()) {
            return internalUpdateTabByPriKey(tab);
        } else {
            List<WxEvertabsTab> tabs = everTabsWorkspaceService.queryTabsByUserIdAndTabId(wxMaUser.getId(), tab.getId());
            if (null == tabs) {
                if (everTabsWorkspaceService.createTab(tab) > 0) {
                    log.info("createTab success {}", tab);
                    genTabsSortValue(tab.getWorkspaceId(), tab);
                    return BaseModel.success(tab);
                } else {
                    log.error("createTab fail {}", tab);
                    return BaseModel.error(ErrorCode.DAO_ERROR);
                }
            } else {
                for (WxEvertabsTab t: tabs) {
                    if (t.getUrl().equals(tab.getUrl())) {
                        // 如果url相等认为更准确
                        tab.setPkId(t.getPkId());
                        tab.setWorkspaceId(t.getWorkspaceId());
                        if (!t.getSort().equals(tab.getSort())) {
                            // 说明sort有更新
                            genTabsSortValue(tab.getWorkspaceId(), tab);
                        }
                        return internalUpdateTabByPriKey(tab);
                    }
                }
                if (tabs.size() > 0) {
                    tab.setPkId(tabs.get(0).getPkId());
                    tab.setWorkspaceId(tabs.get(0).getWorkspaceId());
                    if (!tabs.get(0).getSort().equals(tab.getSort())) {
                        // 说明sort有更新
                        genTabsSortValue(tab.getWorkspaceId(), tab);
                    }
                    return internalUpdateTabByPriKey(tab);
                }
            }
        }
        return BaseModel.error(ErrorCode.NO_RESULT);
    }

    @PostMapping("/updateTabForce")
    public BaseModel<WxEvertabsTab> updateTabForce(HttpServletRequest request,
                                              @RequestBody WxEvertabsTab tab) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        if (everTabsWorkspaceService.updateTab(tab) > 0) {
            return BaseModel.success(tab);
        } else {
            log.error(wxMaUser + " " + tab.toString());
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }

    private List<WorkspaceEx> getCurWorkspaceList(Integer wxMaUserId) {
        List<WxEvertabsWorkspace> workspaces = everTabsWorkspaceService.queryAllWorkspaces(wxMaUserId);
        List<Integer> workspaceIdList = workspaces.stream()
                .map(WxEvertabsWorkspace::getId).collect(Collectors.toList());

        Map<Integer, List<WxEvertabsTab>> workspaceTabsMap =
                everTabsWorkspaceService.queryWorkspaceTabsMap(workspaceIdList);

        return workspaces.stream().map(workspace -> {
            WorkspaceEx workspaceEx = new WorkspaceEx();
            workspaceEx.copyFrom(workspace);
            List<WxEvertabsTab> tabs = workspaceTabsMap.get(workspace.getId());
            if (null != tabs) {
                for (WxEvertabsTab tab : tabs) {
                    // 异步发起抓取后再次刷新页面时读redis里的favIcon并更新
                    if (null != tab.getFavIconUrl()) {
                        Object value = redisTemplate.opsForValue().get(tab.getFavIconUrl());
                        if (null != value) {
                            tab.setFavIconUrl(String.valueOf(value));
                            everTabsWorkspaceService.updateTab(tab);
                        }
                    }
                }
            }
            workspaceEx.setTabs(tabs);
            if (null != workspaceEx.getTabs()) {
                workspaceEx.setTabsCount(workspaceEx.getTabs().size());
            }
            return workspaceEx;
        }).collect(Collectors.toList());
    }

    @PostMapping("/batchUpdateTabs")
    public BaseModel<List<WorkspaceEx>> batchUpdateTabs(HttpServletRequest request,
                                              @RequestParam Integer workspaceId,
                                              @RequestBody List<WxEvertabsTab> tabs) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        try {
            everTabsWorkspaceService.replaceTabs(workspaceId, tabs);
            log.info("batchUpdateTabs success workspaceId={} tabs={}", workspaceId, tabs);
            return BaseModel.success(getCurWorkspaceList(wxMaUser.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("batchUpdateTabs fail workspaceId={} tabs={}", workspaceId, tabs);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }

    @PostMapping("/transToWorkspace")
    public BaseModel<String> transToWorkspace(HttpServletRequest request,
                                              @RequestParam Integer workspaceId,
                                              @RequestParam String name
    ) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        WxEvertabsWorkspace workspace = everTabsWorkspaceService.queryWorkspaceById(workspaceId);
        if (null == workspace || !workspace.getWxMaUserId().equals(wxMaUser.getId())) {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }

        workspace.setName(name);
        workspace.setIsTemp(false);
        everTabsWorkspaceService.update(workspace);
        log.info("transToWorkspace success {}", workspace);
        return BaseModel.success();
    }

    @PostMapping("/delWorkspace")
    public BaseModel<String> delWorkspace(HttpServletRequest request,
                                              @RequestParam Integer workspaceId
    ) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        WxEvertabsWorkspace workspace = everTabsWorkspaceService.queryWorkspaceById(workspaceId);
        if (null == workspace || !workspace.getWxMaUserId().equals(wxMaUser.getId())) {
            log.error("delWorkspace fail workspaceId={}", workspaceId);
            return BaseModel.error(ErrorCode.NO_RESULT);
        }

        if (everTabsWorkspaceService.delete(workspace) > 0) {
            log.info("delWorkspace success {}", workspace);
            return BaseModel.success();
        } else {
            log.error("delWorkspace fail {}", workspace);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }

    @PostMapping("/changeWorkspaceName")
    public BaseModel<String> changeWorkspaceName(HttpServletRequest request,
                                                 @RequestParam Integer workspaceId,
                                                 @RequestParam String name
    ) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        WxEvertabsWorkspace workspace = everTabsWorkspaceService.queryWorkspaceById(workspaceId);
        if (null == workspace || !workspace.getWxMaUserId().equals(wxMaUser.getId())) {
            log.error("changeWorkspaceName fail workspaceId={} name={}", workspaceId, name);
            return BaseModel.error(ErrorCode.NO_RESULT);
        }

        workspace.setName(name);
        everTabsWorkspaceService.update(workspace);
        log.info("changeWorkspaceName success {}", workspace);
        return BaseModel.success();
    }

    @PostMapping("/addFeedback")
    public BaseModel<String> addFeedback(HttpServletRequest request,
                                               @RequestBody WxEvertabsFeedback feedback) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        feedback.setWxMaUserId(wxMaUser.getId());

        if (feedbackService.addFeedback(feedback)) {
            log.info("addFeedback success {}", feedback);
            return BaseModel.success();
        } else {
            log.error("addFeedback fail {}", feedback);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }
}
