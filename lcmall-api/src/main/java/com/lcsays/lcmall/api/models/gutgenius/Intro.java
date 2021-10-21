package com.lcsays.lcmall.api.models.gutgenius;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: lichuang
 * @Date: 21-8-11 12:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Intro implements Serializable {
    private Long id;
    private String tabName;
    private String imageUrl;
    private String text;
    private Integer sort;
}
