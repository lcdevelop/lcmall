package com.lcsays.lcmall.api.controller.evertabs;

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

        return wxMaUserService.queryUsersByToken(tokenValue);
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
            return BaseModel.success(workspaceEx);
        } else {
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

        return BaseModel.success(everTabsWorkspaceService.queryAllTabsByWorkspaceId(workspaceId));
    }

    @PostMapping("/updateTab")
    public BaseModel<WxEvertabsTab> updateTab(HttpServletRequest request,
                                               @RequestBody WxEvertabsTab tab) {
        WxMaUser wxMaUser = check(request);
        if (null == wxMaUser) {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }

        if (everTabsWorkspaceService.updateTab(tab) > 0) {
            return BaseModel.success(tab);
        } else {
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

        log.info(String.valueOf(workspaceId));
        log.info(String.valueOf(tabs));
        try {
            everTabsWorkspaceService.replaceTabs(workspaceId, tabs);
            return BaseModel.success();
        } catch (Exception e) {
            e.printStackTrace();
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
            return BaseModel.error(ErrorCode.NO_RESULT);
        }

        if (everTabsWorkspaceService.delete(workspace) > 0) {
            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.DAO_ERROR);
        }
    }
}
