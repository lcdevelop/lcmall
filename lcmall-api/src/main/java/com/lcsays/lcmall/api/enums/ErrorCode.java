package com.lcsays.lcmall.api.enums;

/**
 * @Author: lichuang
 * @Date: 21-8-6 13:30
 */

public enum ErrorCode {
    PARAM_ERROR(401, "param error"),
    NO_USER(402, "no user"),
    NO_RESULT(404, "no result"),
    ORDER_PAID(405, "order paid"),
    DELETE_FORBIDDEN(406, "you can not delete"),
    NO_AUTHORITY(407, "you have no authority"),
    GAME_OVER(408, "game is over"),
    WX_SERVICE_ERROR(501, "weixin service error"),
    DAO_ERROR(502, "database operation error"),
    UNKNOWN_ERROR(503, "unknown error"),
    NEED_LOGIN(700, "need login"),
    SUCCESS(200, "success");

    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
