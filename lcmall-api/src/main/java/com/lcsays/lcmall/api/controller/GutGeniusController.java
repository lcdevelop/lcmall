package com.lcsays.lcmall.api.controller;

import com.lcsays.lcmall.api.models.gutgenius.Intro;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.service.gutgenius.GutGeniusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 12:33
 */

@Slf4j
@RestController
@RequestMapping("/api/gutgenius")
public class GutGeniusController {

    @Resource
    GutGeniusService gutGeniusService;

    @GetMapping("/intros")
    public BaseModel<List<Intro>> intros() {
        List<Intro> intros = gutGeniusService.getIntros();
        return BaseModel.success(intros);
    }
}
