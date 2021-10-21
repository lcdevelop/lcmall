package com.lcsays.lcmall.api.service.ec.impl;

import com.google.gson.Gson;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.request.QueryTrackParam;
import com.kuaidi100.sdk.request.QueryTrackReq;
import com.kuaidi100.sdk.response.QueryTrackData;
import com.kuaidi100.sdk.response.QueryTrackResp;
import com.kuaidi100.sdk.utils.SignUtils;
import com.lcsays.lcmall.api.dao.ec.ExpressDao;
import com.lcsays.lcmall.api.models.ec.ExpressType;
import com.lcsays.lcmall.api.service.ec.ExpressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-9-13 21:02
 */
@Service
@Slf4j
public class ExpressServiceImpl implements ExpressService {

    @Resource
    ExpressDao expressDao;

    @Override
    public List<ExpressType> getAllExpressTypes() {
        return expressDao.getAllExpressTypes();
    }

    @Override
    public ExpressType getExpressTypeById(Long id) {
        return expressDao.getExpressTypeById(id);
    }

    @Override
    public List<QueryTrackData> expressTrack(String code, String no, String phone) {
        QueryTrack queryTrack = new QueryTrack();
        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(code);
        queryTrackParam.setNum(no);
        queryTrackParam.setPhone(phone);

        String param = new Gson().toJson(queryTrackParam);
        queryTrackReq.setParam(param);
        String custom = "8C698096B6688C71A15158FFAD071771";
        String key = "nNVhScet6258";
        queryTrackReq.setCustomer(custom);
        queryTrackReq.setSign(SignUtils.querySign(param, key, custom));

        try {
            QueryTrackResp res = queryTrack.queryTrack(queryTrackReq);
            if (res.getStatus().equals("200")) {
                return res.getData();
            } else {
                log.error("expressTrack error status: " + res.getStatus());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
