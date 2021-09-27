package com.lcsays.gg.models.ec;

import com.lcsays.gg.models.manager.WxApp;
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
public class Category implements Serializable {
    private Long id;
    private WxApp wxApp;
    private String name;

    public Category(WxApp wxApp, String name) {
        this.wxApp = wxApp;
        this.name = name;
    }
}
