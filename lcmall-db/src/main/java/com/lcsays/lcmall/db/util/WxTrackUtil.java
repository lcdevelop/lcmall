package com.lcsays.lcmall.db.util;

import com.lcsays.lcmall.db.model.WxTrack;

import java.util.Date;

public class WxTrackUtil {

    public static WxTrack normalizeTime(WxTrack wxTrack) {
        Date createTime = wxTrack.getCreateTime();
        wxTrack.setCreateTime(new Date(createTime.getTime() + 86400000/3));
        return wxTrack;
    }
}
