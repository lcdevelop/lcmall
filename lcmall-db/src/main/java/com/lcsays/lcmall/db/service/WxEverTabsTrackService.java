package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxEvertabsTrackMapper;
import com.lcsays.lcmall.db.model.WxEvertabsTrack;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: lichuang
 * @Date: 21-10-9 11:12
 */
@Service
public class WxEverTabsTrackService {

    @Resource
    private WxEvertabsTrackMapper trackMapper;

    public boolean addTrack(WxEvertabsTrack track) {
        int ret = trackMapper.insertSelective(track);
        if (ret > 0) {
            return true;
        } else {
            return false;
        }
    }
}
