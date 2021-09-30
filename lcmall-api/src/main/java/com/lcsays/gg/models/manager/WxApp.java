package com.lcsays.gg.models.manager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: lichuang
 * @Date: 21-8-26 15:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxApp implements Serializable {
    private Long id;
    private String name;
    private String appId;
    private String image;
    private Integer auth; // 是否需要授权才能有操作
    private String wxaCodeUrl;
    private String qrCodePictureUrl;
    private String type; // 根据不同type确定前端哪些版块可见，比如marketing
    private String mchId; // 关联的支付商户id
}
