package com.lcsays.lcmall.api.models.result;

import com.lcsays.lcmall.api.enums.ErrorCode;
import lombok.Data;

/**
 * @Author: lichuang
 * @Date: 21-8-6 13:22
 */
@Data
public class BaseModel<T> {

    private Integer code = 200;
    private String msg = "";
    private T data = null;
    private int total = 0; // 这个不要去掉，因为前端如果是表格，系统自动会用到计算翻页上

    public static <T> BaseModel<T> success() {
        BaseModel<T> res = new BaseModel<>();
        res.setMsg("success");
        return res;
    }

    public static <T> BaseModel<T> success(T data) {
        BaseModel<T> res = new BaseModel<>();
        res.setData(data);
        res.setMsg("success");
        return res;
    }

    public static <T> BaseModel<T> success(T data, int total) {
        BaseModel<T> res = new BaseModel<>();
        res.setData(data);
        res.setTotal(total);
        res.setMsg("success");
        return res;
    }

    public static <T> BaseModel<T> error(ErrorCode errorCode) {
        BaseModel<T> res = new BaseModel<>();
        res.setCode(errorCode.getCode());
        res.setMsg(errorCode.getMsg());
        return res;
    }

    public static <T> BaseModel<T> errorWithMsg(ErrorCode errorCode, String msg) {
        BaseModel<T> res = new BaseModel<>();
        res.setCode(errorCode.getCode());
        res.setMsg(msg);
        return res;
    }

    public static <T> BaseModel<T> error(ErrorCode errorCode, T data) {
        BaseModel<T> res = new BaseModel<>();
        res.setCode(errorCode.getCode());
        res.setMsg(errorCode.getMsg());
        res.setData(data);
        return res;
    }
}
