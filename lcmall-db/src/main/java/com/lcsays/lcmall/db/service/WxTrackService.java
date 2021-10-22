package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxTrackMapper;
import com.lcsays.lcmall.db.model.WxTrack;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: lichuang
 * @Date: 21-9-27 16:10
 */
@Service
public class WxTrackService {

    @Resource
    private WxTrackMapper wxTrackMapper;

    public void track(WxTrack track) {
        track.setCreateTime(new Date());
        wxTrackMapper.insert(track);
    }
}
