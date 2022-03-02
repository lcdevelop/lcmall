package com.lcsays.lcmall.api.controller.evertabs;

import com.lcsays.lcmall.api.config.logger.UserIdLogConverter;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.evertabs.WorkspaceEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.CookieTokenUtils;
import com.lcsays.lcmall.db.model.WxEvertabsTab;
import com.lcsays.lcmall.db.model.WxEvertabsWorkspace;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxEverTabsWorkspaceService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

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
    WxMaUserService wxMaUserService;

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

        List<WxEvertabsWorkspace> workspaces = everTabsWorkspaceService.queryAllWorkspaces(wxMaUser.getId());
        List<Integer> workspaceIdList = workspaces.stream()
                .map(WxEvertabsWorkspace::getId).collect(Collectors.toList());
        Map<Integer, Long> workspaceTabsCountMap = everTabsWorkspaceService.workspaceTabsCount(workspaceIdList);
        return BaseModel.success(workspaces.stream().map(workspace -> {
            WorkspaceEx workspaceEx = new WorkspaceEx();
            workspaceEx.copyFrom(workspace);
            workspaceEx.setTabsCount(workspaceTabsCountMap.get(workspace.getId()));
            return workspaceEx;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/tabList")
    public BaseModel<List<WxEvertabsTab>> tabList(HttpServletRequest request, @RequestParam Integer workspaceId) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        List<WxEvertabsTab> tabs = everTabsWorkspaceService.queryAllTabsByWorkspaceId(workspaceId);
        log.info("tabList workspaceId={} tabs.size={}", workspaceId, tabs.size());
        return BaseModel.success(tabs);
    }

    @PostMapping("/updateTab")
    public BaseModel<WxEvertabsTab> updateTab(HttpServletRequest request,
                                               @RequestBody WxEvertabsTab tab) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        if (everTabsWorkspaceService.updateTab(tab) > 0) {
            log.info("updateTab success {}", tab);
            return BaseModel.success(tab);
        } else {
            log.error("updateTab fail {}", tab);
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }

    @PostMapping("/batchUpdateTabs")
    public BaseModel<String> batchUpdateTabs(HttpServletRequest request,
                                              @RequestParam Integer workspaceId,
                                              @RequestBody List<WxEvertabsTab> tabs) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        try {
            everTabsWorkspaceService.replaceTabs(workspaceId, tabs);
            log.info("batchUpdateTabs success workspaceId={} tabs={}", workspaceId, tabs);
            return BaseModel.success();
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
}
