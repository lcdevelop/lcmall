package com.lcsays.lcmall.api.controller.evertabs;

import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.CookieTokenUtils;
import com.lcsays.lcmall.api.utils.TimeUtils;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.service.WxMaUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

/**
 * @Author: lichuang
 * @Date: 2022/2/17 21:03
 */
@Slf4j
@RestController
@RequestMapping("/api/evertabs/user")
public class UserController {

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private String genRandomCode() {
        Random random = new Random(new Date().getTime());
        StringBuilder ret = new StringBuilder();
        int r;
        for (int i = 0; i < 6; i++) {
            r = random.nextInt(10);
            ret.append(r);
        }
        return ret.toString();
    }

    @PostMapping("/sendCode")
    public BaseModel<String> sendCode(@RequestBody WxMaUser body) {
        WxMaUser user = wxMaUserService.queryUserByEmail(body.getEmail());
        if (null == user) {
            if (null == body.getNickname() || "".equals(body.getNickname())) {
                body.setNickname("匿名用户");
            }
            user = wxMaUserService.createUserWithEmail(body.getEmail());
        }

        if (null != user) {
            String code = genRandomCode();
            user.setCode(code);
            user.setCodeExpire(TimeUtils.nSecondLater(10 * 60));
            wxMaUserService.update(user);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(body.getEmail());
            message.setSubject("EverTabs注册验证");
            message.setText("验证码：" + code);
            mailSender.send(message);

            return BaseModel.success();
        } else {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }
    }

    @PostMapping("/register")
    public BaseModel<String> register(@RequestBody WxMaUser body) {
        WxMaUser user = wxMaUserService.queryUserByEmail(body.getEmail());
        if (null == user) {
            return BaseModel.error(ErrorCode.CODE_ERROR);
        }

        if ("".equals(user.getCode())) {
            return BaseModel.error(ErrorCode.CODE_ERROR);
        }

        if (!user.getCode().equals(body.getCode())) {
            return BaseModel.error(ErrorCode.CODE_ERROR);
        }

        String password = body.getPassword();
        if (null == password || "".equals(password)) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        user.setPassword(body.getPassword());
        user.setCode("");
        wxMaUserService.update(user);
        return BaseModel.success();
    }

    @PostMapping("/login")
    public BaseModel<WxMaUser> login(HttpServletResponse response, @RequestBody WxMaUser body) {
        WxMaUser user = wxMaUserService.queryUserByEmail(body.getEmail());
        if (null == user) {
            return BaseModel.error(ErrorCode.NO_RESULT);
        }

        if (!user.getPassword().equals(body.getPassword())) {
            return BaseModel.error(ErrorCode.NO_AUTHORITY);
        }

        String token = CookieTokenUtils.setTokenValue(response);
        user.setToken(token);
        wxMaUserService.update(user);
        return BaseModel.success(user);
    }
}
