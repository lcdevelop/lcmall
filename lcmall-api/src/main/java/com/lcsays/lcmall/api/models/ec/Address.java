package com.lcsays.lcmall.api.models.ec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: lichuang
 * @Date: 21-8-11 18:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    private Long id;

    private Long wxMaUserId;
    private String name;
    private String phone;
    private String address;

    public Address(Long wxMaUserId, String name, String phone, String address) {
        this.wxMaUserId = wxMaUserId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}
