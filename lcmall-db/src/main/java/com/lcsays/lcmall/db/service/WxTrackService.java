package com.lcsays.lcmall.db.service;

import com.lcsays.lcmall.db.dao.WxTrackMapper;
import com.lcsays.lcmall.db.model.WxTrack;
import com.lcsays.lcmall.db.model.WxTrackExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    public List<WxTrack> queryByActivityIdAndDateRange(Integer activityId, Date begin, Date end) {
        WxTrackExample example = new WxTrackExample();
        WxTrackExample.Criteria criteria = example.createCriteria();
        criteria.andMsgLike("%activityId=" + activityId + "%");
        criteria.andCreateTimeBetween(begin, end);
        return wxTrackMapper.selectByExample(example);
    }

    public List<WxTrack> queryByActivityId(Integer activityId) {
        WxTrackExample example = new WxTrackExample();
        WxTrackExample.Criteria criteria = example.createCriteria();
        criteria.andMsgLike("%activityId=" + activityId + "&%");
        WxTrackExample.Criteria criteria2 = example.createCriteria();
        criteria2.andMsgLike("%activityId=" + activityId);
        example.or(criteria2);
        return wxTrackMapper.selectByExample(example);
    }

    public List<WxTrack> queryByActivityIdAndEventType(Integer activityId, String eventType) {
        WxTrackExample example = new WxTrackExample();
        WxTrackExample.Criteria criteria = example.createCriteria();
        criteria.andEventTypeEqualTo(eventType);
        criteria.andMsgLike("%activityId=" + activityId + "&%");
        WxTrackExample.Criteria criteria2 = example.createCriteria();
        criteria2.andEventTypeEqualTo(eventType);
        criteria2.andMsgLike("%activityId=" + activityId);
        example.or(criteria2);
        return wxTrackMapper.selectByExample(example);
    }

    public List<WxTrack> queryByWxMaUserId(Integer wxMaUserId) {
        WxTrackExample example = new WxTrackExample();
        WxTrackExample.Criteria criteria = example.createCriteria();
        criteria.andWxMaUserIdEqualTo(wxMaUserId);
        return wxTrackMapper.selectByExample(example);
    }
}
