package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxEvertabsFeedbackMapper;
import com.lcsays.lcmall.db.model.WxEvertabsFeedback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: lichuang
 * @Date: 21-10-9 11:12
 */
@Service
public class WxEverTabsFeedbackService {

    @Resource
    private WxEvertabsFeedbackMapper feedbackMapper;

    public boolean addFeedback(WxEvertabsFeedback feedback) {
        int ret = feedbackMapper.insertSelective(feedback);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }
}
