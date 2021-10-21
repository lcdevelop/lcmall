package com.lcsays.lcmall.api.models.result;

import lombok.Data;

/**
 * @Author: lichuang
 * @Date: 21-8-6 13:22
 */
@Data
public class WxResp {

    private static final String CODE_SUCCESS = "SUCCESS";
    private static final String CODE_FAIL = "FAIL";

    private String code = CODE_SUCCESS;
    private String message = "";

    public static WxResp success() {
        WxResp res = new WxResp();
        res.setCode(CODE_SUCCESS);
        res.setMessage("成功");
        return res;
    }

    public static WxResp error(String message) {
        WxResp res = new WxResp();
        res.setCode(CODE_FAIL);
        res.setMessage(message);
        return res;
    }

}
