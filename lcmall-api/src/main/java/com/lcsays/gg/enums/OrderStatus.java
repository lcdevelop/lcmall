package com.lcsays.gg.enums;

/**
 * @Author: lichuang
 * @Date: 21-8-6 13:30
 */

public enum OrderStatus {
    OS_INIT(0, "待付款"),
    OS_PAID(1, "待发货"),
    OS_TRANSIT(2, "待收货"),
    OS_REFUND(3, "退款/售后"),
    OS_SUCCESS(4, "已完成");

    private final int value;
    private final String msg;

    OrderStatus(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public static String getStatusStr(int value) {
        switch (value) {
            case 0:
                return OS_INIT.msg;
            case 1:
                return OS_PAID.msg;
            case 2:
                return OS_TRANSIT.msg;
            case 3:
                return OS_REFUND.msg;
            case 4:
                return OS_SUCCESS.msg;
            default:
                return OS_SUCCESS.msg;
        }
    }
}
