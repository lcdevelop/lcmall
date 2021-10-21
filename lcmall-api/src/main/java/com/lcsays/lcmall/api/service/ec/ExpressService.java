package com.lcsays.lcmall.api.service.ec;

import com.kuaidi100.sdk.response.QueryTrackData;
import com.lcsays.lcmall.api.models.ec.ExpressType;

import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 21:02
 */

public interface ExpressService {
    List<ExpressType> getAllExpressTypes();
    ExpressType getExpressTypeById(Long id);
    List<QueryTrackData> expressTrack(String code, String no, String phone);
}
