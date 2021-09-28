package com.lcsays.gg.controller.manager;

import com.lcsays.gg.config.OSSConfiguration;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.manager.AppService;
import com.lcsays.gg.utils.OssUtils;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.service.WxAppService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-26 11:23
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/manager")
@EnableConfigurationProperties(OSSConfiguration.class)
public class ManagerController {

    @Resource
    AppService appService;

    @Resource
    WxMpService wxMpService;

    @Resource
    WxAppService wxAppService;

    private OSSConfiguration ossConfiguration;

    @GetMapping("/appList")
    public BaseModel<List<WxApp>> appList(HttpSession session) {
        String shortSid = SessionUtils.normalizeSessionId(session);

        List<com.lcsays.lcmall.db.model.WxApp> apps1 = wxAppService.appList();
        log.info(apps1.toString());

        List<WxApp> apps = appService.apps();
        for (WxApp app: apps) {
            try {
                // 这里形成的sessionId也就是回调的eventKey格式类似：wxfe9faf8c8e3a5830_68822c00-7ca8-4762-9ffc-d1bd455fe49d
                String sceneStr = SessionUtils.addPrefix(shortSid, app.getAppId());
                WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(
                    sceneStr,
                    10000
                );
                log.info("appList " + app.getName() + " " + sceneStr);
                String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
                app.setQrCodePictureUrl(url);
            } catch (WxErrorException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.error(ErrorCode.WX_SERVICE_ERROR);
            }
        }
        return BaseModel.success(apps);
    }

    @PostMapping("/uploadImage")
    public BaseModel<String> uploadImage(HttpSession session, @RequestParam("image") MultipartFile file) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user && user.checkAuthority()) {
            if (!file.isEmpty()) {
                log.info(file.getOriginalFilename()); // logo.svg
                log.info(file.getName());  // image
                String url = OssUtils.uploadFile(
                    file,
                    ossConfiguration.getEndpoint(),
                    ossConfiguration.getKeyId(),
                    ossConfiguration.getKeySecret(),
                    ossConfiguration.getBucketName(),
                    ossConfiguration.getFileHost()
                );
                return BaseModel.success(url);
            } else {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }
}
