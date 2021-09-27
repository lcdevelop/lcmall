package com.lcsays.gg.models.ec;

import com.lcsays.gg.enums.OrderStatus;
import com.lcsays.gg.models.manager.WxApp;
import com.lcsays.gg.models.weixin.WxMaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 18:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    private Long id;

    private WxApp wxApp;
    private WxMaUser wxMaUser;
    private Address address;
    private String outTradeNo;
    private String transactionId;
    private Integer totalFee;
    private Integer status;
    private String tradeStatus;
    private ExpressType expressType;
    private String expressNo;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp successTime;

    private String statusStr;

    private List<OrderAffiliate> affiliateList;

    public Order(WxApp wxApp, WxMaUser wxMaUser, Address address, String outTradeNo, Integer totalFee, Integer status) {
        this.wxApp = wxApp;
//        this.wxMaUserId = userId;
//        this.addressId = addressId;
        this.wxMaUser = wxMaUser;
        this.address = address;
        this.outTradeNo = outTradeNo;
        this.totalFee = totalFee;
        this.status = status;
    }

    public String getStatusStr() {
        return OrderStatus.getStatusStr(this.status);
    }
}
