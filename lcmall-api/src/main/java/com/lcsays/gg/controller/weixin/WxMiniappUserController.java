package com.lcsays.gg.controller.weixin;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.lcsays.gg.config.WxMaConfiguration;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.ec.Address;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.service.manager.AppService;
import com.lcsays.gg.service.weixin.UserService;
import com.lcsays.gg.utils.SessionUtils;
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
    AppService appService;

    @Resource
    UserService userService;
    
    @Resource
    WxMaService wxMaService;

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
            log.info(sessionInfo.getSessionKey());
            log.info(sessionInfo.getOpenid());
            WxApp wxApp = appService.getWxAppByAppId(appId);
            if (null == wxApp) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }
            WxMaUser wxMaUser = userService.getWxMaUserByOpenid(wxApp, sessionInfo.getOpenid());
            if (null != wxMaUser) {
                log.info("" + wxMaUser);
                wxMaUser.parseFrom(sessionInfo);
                userService.update(wxMaUser);
            } else {
                wxMaUser = new WxMaUser(wxApp);
                wxMaUser.parseFrom(sessionInfo);
                long count = userService.insert(wxMaUser);
                if (count > 0) {
                    log.info("wxMaUserDao.insert success userId: " + wxMaUser);
                } else {
                    log.info("wxMaUserDao.insert error: " + wxMaUser);
                }

            }

            // 把用户信息存到session里
            SessionUtils.saveUserToSession(session, wxMaUser);
            return BaseModel.success();
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return BaseModel.error(ErrorCode.WX_SERVICE_ERROR);
        }
    }

    @GetMapping("/user_profile")
    public BaseModel<String> userProfile(@PathVariable String appId, Long userId,
                       String signature, String rawData, String encryptedData, String iv) {
        log.info(appId);
        log.info(String.valueOf(userId));
        log.info(signature);
        log.info(rawData);
        log.info(encryptedData);
        log.info(iv);

        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            if (wxMaService.switchoverTo(appId)
                    .getUserService().checkUserInfo(wxMaUser.getSessionKey(), rawData, signature)) {
                WxMaUserInfo userInfo = wxMaService.switchoverTo(appId)
                        .getUserService().getUserInfo(wxMaUser.getSessionKey(), encryptedData, iv);
                wxMaUser.parseFrom(userInfo);
                userService.update(wxMaUser);
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
        WxMaUser wxMaUser = SessionUtils.getUserFromSession(session);
        if (null != wxMaUser) {
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.switchoverTo(appId)
                    .getUserService().getPhoneNoInfo(
                            wxMaUser.getSessionKey(),
                            decryptPhoneNumberParam.getEncryptedData(),
                            decryptPhoneNumberParam.getIv()
                    );
            log.info(phoneNoInfo.toString());
            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/get_user_display")
    public BaseModel<WxMaUser> getUserDisplay(HttpSession session, @PathVariable String appId) {
        WxMaUser wxMaUser = SessionUtils.getUserFromSession(session);
        if (null != wxMaUser) {
            wxMaUser.clearSecret();
            return BaseModel.success(wxMaUser);
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }


    @GetMapping("/get_addresses")
    public BaseModel<List<Address>> getAddresses(@PathVariable String appId,
                                                 @RequestParam("userId") Long userId) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            List<Address> addresses = userService.getAddressesByUser(wxMaUser);
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
    public BaseModel<Address> getAddressById(@RequestParam("addressId") Long addressId) {
        Address address = userService.getAddressById(addressId);
        if (address != null) {
            return BaseModel.success(address);
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }

    @PostMapping("/add_address")
    public BaseModel<String> addAddress(@PathVariable String appId,
                                        @RequestParam("userId") Long userId,
                                        @RequestParam("name") String name,
                                        @RequestParam("phone") String phone,
                                        @RequestParam("address") String address) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            Address addr = new Address(wxMaUser.getId(), name, phone, address);
            if (userService.addAddress(addr) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @PostMapping("/update_address")
    public BaseModel<String> updateAddress(@RequestParam("addressId") Long addressId,
                                           @RequestParam("name") String name,
                                           @RequestParam("phone") String phone,
                                           @RequestParam("address") String address) {
        Address addr = userService.getAddressById(addressId);
        if (null != addr) {
            addr.setName(name);
            addr.setPhone(phone);
            addr.setAddress(address);
            if (userService.updateAddress(addr) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }

    @PostMapping("/del_address")
    public BaseModel<String> delAddress(@RequestParam("addressId") Long addressId) {
        Address addr = userService.getAddressById(addressId);
        if (null != addr) {
            if (userService.delAddress(addr.getId()) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }
}
