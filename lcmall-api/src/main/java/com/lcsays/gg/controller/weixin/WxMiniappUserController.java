package com.lcsays.gg.controller.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.model.EcAddress;
import com.lcsays.lcmall.db.model.WxApp;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.EcAddressService;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-5 18:47
 * 注解@RequestBody对应客户端要用postJson，@RequestParam对应客户端要用post/fetch
 */
@Slf4j
@RestController
@RequestMapping("/api/wx/ma/{appId}/user")
public class WxMiniappUserController {

    @Resource
    WxAppService wxAppService;

    @Resource
    WxMaService wxMaService;

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    EcAddressService ecAddressService;

    @Data
    private static class DecryptPhoneNumberParam {
        private String encryptedData;
        private String iv;
    }

    @GetMapping("/login")
    public BaseModel<String> login(HttpSession session, @PathVariable String appId, String code) {
        if (StringUtils.isBlank(code)) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.switchoverTo(appId).getUserService().getSessionInfo(code);
            log.debug("SessionKey: " + sessionInfo.getSessionKey());
            log.debug("Openid: " + sessionInfo.getOpenid());
            log.debug("Unionid: " + sessionInfo.getUnionid());
            WxApp wxApp = wxAppService.queryByAppId(appId);
            if (null == wxApp) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }

            WxMaUser wxMaUser = wxMaUserService.getWxMaUserByOpenid(wxApp, sessionInfo.getOpenid());

            if (null != wxMaUser) {
                log.debug("" + wxMaUser);
                wxMaUser.setOpenid(sessionInfo.getOpenid());
                wxMaUser.setUnionid(sessionInfo.getUnionid());
                wxMaUser.setSessionKey(sessionInfo.getSessionKey());
                wxMaUserService.update(wxMaUser);
            } else {
                wxMaUser = new WxMaUser();
                wxMaUser.setWxAppId(wxApp.getId());
                wxMaUser.setOpenid(sessionInfo.getOpenid());
                wxMaUser.setUnionid(sessionInfo.getUnionid());
                wxMaUser.setSessionKey(sessionInfo.getSessionKey());
                long count = wxMaUserService.insert(wxMaUser);
                if (count > 0) {
                    log.info("wxMaUserDao.insert success user: " + wxMaUser);
                } else {
                    log.info("wxMaUserDao.insert error: " + wxMaUser);
                }

            }

            // 把用户信息存到session里
            SessionUtils.saveWxUserToSession(session, wxMaUser);
            return BaseModel.success();
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return BaseModel.error(ErrorCode.WX_SERVICE_ERROR);
        }
    }

    @GetMapping("/user_profile")
    public BaseModel<String> userProfile(HttpSession session, @PathVariable String appId,
                       String signature, String rawData, String encryptedData, String iv) {

        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        if (null != wxMaUser) {
            if (wxMaService.switchoverTo(appId)
                    .getUserService().checkUserInfo(wxMaUser.getSessionKey(), rawData, signature)) {
                WxMaUserInfo userInfo = wxMaService.switchoverTo(appId)
                        .getUserService().getUserInfo(wxMaUser.getSessionKey(), encryptedData, iv);
                wxMaUser.setNickname(userInfo.getNickName());
                wxMaUser.setAvatarUrl(userInfo.getAvatarUrl());
                wxMaUser.setGender(userInfo.getGender());
                wxMaUser.setCountry(userInfo.getCountry());
                wxMaUser.setCity(userInfo.getCity());
                wxMaUser.setLanguage(userInfo.getLanguage());
                wxMaUserService.update(wxMaUser);
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.NEED_LOGIN);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }


    @PostMapping("/decryptPhoneNumber")
    public BaseModel<String> decryptPhoneNumber(HttpSession session,
                                         @PathVariable String appId,
                                         @RequestBody DecryptPhoneNumberParam decryptPhoneNumberParam) {
        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        if (null != wxMaUser) {
            log.info(wxMaUser.toString());
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.switchoverTo(appId)
                    .getUserService().getPhoneNoInfo(
                            wxMaUser.getSessionKey(),
                            decryptPhoneNumberParam.getEncryptedData(),
                            decryptPhoneNumberParam.getIv()
                    );
            log.info(phoneNoInfo.toString());
            wxMaUserService.updatePhoneNumber(wxMaUser.getId(), phoneNoInfo.getPhoneNumber());
            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/get_user_display")
    public BaseModel<WxMaUser> getUserDisplay(HttpSession session, @PathVariable String appId) {
        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        if (null != wxMaUser) {
            // 需要复查，因为可能有更新
            wxMaUser = wxMaUserService.getWxMaUserById(wxMaUser.getId());
            SessionUtils.saveWxUserToSession(session, wxMaUser);

            log.debug("getUserDisplay: " + wxMaUser);

            WxMaUser ret = new WxMaUser();
            ret.setNickname(wxMaUser.getNickname());
            ret.setAvatarUrl(wxMaUser.getAvatarUrl());
            ret.setGender(wxMaUser.getGender());
            ret.setCountry(wxMaUser.getCountry());
            ret.setCity(wxMaUser.getCity());
            ret.setLanguage(wxMaUser.getLanguage());
            ret.setPhoneNumber(wxMaUser.getPhoneNumber());
            ret.setRole(wxMaUser.getRole());
            return BaseModel.success(ret);
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }


    @GetMapping("/get_addresses")
    public BaseModel<List<EcAddress>> getAddresses(HttpSession session, @PathVariable String appId) {
        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        if (null != wxMaUser) {
            List<EcAddress> addresses = ecAddressService.getAddressesByUser(wxMaUser);
            if (addresses != null) {
                return BaseModel.success(addresses);
            } else {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/get_address_by_id")
    public BaseModel<EcAddress> getAddressById(@RequestParam("addressId") Integer addressId) {
        EcAddress address = ecAddressService.getAddressById(addressId);
        if (address != null) {
            return BaseModel.success(address);
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }

    @PostMapping("/add_address")
    public BaseModel<String> addAddress(HttpSession session,
                                        @PathVariable String appId,
                                        @RequestParam("name") String name,
                                        @RequestParam("phone") String phone,
                                        @RequestParam("address") String address) {

        WxApp wxApp = wxAppService.queryByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        WxMaUser wxMaUser = SessionUtils.getWxUserFromSession(session);
        if (null != wxMaUser) {
            EcAddress addr = new EcAddress();
            addr.setWxMaUserId(wxMaUser.getId());
            addr.setName(name);
            addr.setPhone(phone);
            addr.setAddress(address);
            if (ecAddressService.addAddress(addr) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @PostMapping("/update_address")
    public BaseModel<String> updateAddress(@RequestParam("addressId") Integer addressId,
                                           @RequestParam("name") String name,
                                           @RequestParam("phone") String phone,
                                           @RequestParam("address") String address) {
        EcAddress addr = ecAddressService.getAddressById(addressId);
        if (null != addr) {
            addr.setName(name);
            addr.setPhone(phone);
            addr.setAddress(address);
            if (ecAddressService.updateAddress(addr) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }

    @PostMapping("/del_address")
    public BaseModel<String> delAddress(@RequestParam("addressId") Integer addressId) {
        EcAddress addr = ecAddressService.getAddressById(addressId);
        if (null != addr) {
            if (ecAddressService.delAddress(addr.getId()) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }
}
