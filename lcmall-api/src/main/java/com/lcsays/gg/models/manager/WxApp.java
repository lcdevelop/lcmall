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
    private Integer auth;
    private String wxaCodeUrl;
    private String qrCodePictureUrl;
}
