package com.lcsays.gg.controller.manager;

import com.github.binarywang.wxpay.bean.marketing.*;
import com.github.binarywang.wxpay.bean.marketing.enums.StockTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.RsaCryptoUtil;
import com.google.gson.annotations.SerializedName;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.utils.ApiUtils;
import com.lcsays.gg.utils.RequestNo;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.lcmall.db.model.WxMaUser;
import com.lcsays.lcmall.db.model.WxMarketingStock;
import com.lcsays.lcmall.db.model.WxMarketingWhitelist;
import com.lcsays.lcmall.db.service.WxAppService;
import com.lcsays.lcmall.db.service.WxMarketingStockService;
import com.lcsays.lcmall.db.service.WxMarketingWhitelistService;
import com.lcsays.lcmall.db.util.WxMaUserUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.lcsays.gg.utils.TimeUtils.timeStr2Rfc3399;

/**
 * @Author: lichuang
 * @Date: 21-9-29 12:36
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/manager/marketing")
public class ManagerMarketingController {

    private static final Gson GSON = new GsonBuilder().create();

    @Resource
    private WxPayService wxPayService;

    @Resource
    private WxMarketingStockService wxMarketingStockService;

    @Resource
    private WxAppService wxAppService;

    @Resource
    WxMarketingWhitelistService wxMarketingWhitelistService;

    @GetMapping("/whitelist")
    public BaseModel<List<WxMarketingWhitelist>> whitelist(HttpSession session,
                                                           @RequestParam("current") Integer current,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           String phoneNumber) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (WxMaUserUtil.checkAuthority(user, wxAppService)) {
                List<WxMarketingWhitelist> data;
                if (!StringUtils.isEmpty(phoneNumber)) {
                    data = wxMarketingWhitelistService.queryByPhoneNumber(phoneNumber);
                } else {
                    data = wxMarketingWhitelistService.queryAll();
                }
                return BaseModel.success(ApiUtils.pagination(data, current, pageSize), data.size());
            } else {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/stock")
    public BaseModel<List<FavorStocksGetResult>> getStocks(HttpSession session,
                                                           @RequestParam("current") Integer current,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam("status") String status) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            try {
                String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
                FavorStocksQueryRequest request = new FavorStocksQueryRequest();
                request.setOffset(current-1);
                request.setLimit(pageSize);
                request.setStockCreatorMchid(curMchId);
                request.setStatus(status);
                log.info(request.toString());
                FavorStocksQueryResult res = wxPayService.switchoverTo(curMchId)
                        .getMarketingFavorService().queryFavorStocksV3(request);
                return BaseModel.success(res.getData(), res.getTotalCount());
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                if ("RESOURCE_NOT_EXISTS".equals(e.getErrCode())) {
                   return BaseModel.success(new ArrayList<>(), 0);
                } else {
                    return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
                }
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/stock")
    public BaseModel<String> addStock(HttpSession session, @RequestBody FavorStocksCreateRequest request) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            try {
                request.setBelongMerchant(curMchId);
                request.setStockType(StockTypeEnum.NORMAL);
                request.setAvailableBeginTime(timeStr2Rfc3399(request.getAvailableBeginTime()));
                request.setAvailableEndTime(timeStr2Rfc3399(request.getAvailableEndTime()));
                request.setOutRequestNo(RequestNo.randomWithCurTime("cs"));
                FavorStocksCreateResult res = wxPayService.switchoverTo(curMchId).getMarketingFavorService().createFavorStocksV3(request);
                return BaseModel.success(res.getStockId());
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/stockDetail")
    public BaseModel<FavorStocksGetResult> stockDetail(HttpSession session, @RequestParam("stockId") String stockId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            try {
                FavorStocksGetResult res = wxPayService.switchoverTo(curMchId).getMarketingFavorService().getFavorStocksV3(stockId, curMchId);
                return BaseModel.success(res);
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/startStock")
    public BaseModel<FavorStocksStartResult> startStock(HttpSession session, @RequestParam("stockId") String stockId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            try {
                FavorStocksSetRequest request = new FavorStocksSetRequest();
                request.setStockCreatorMchid(curMchId);
                FavorStocksStartResult res = wxPayService.switchoverTo(curMchId)
                        .getMarketingFavorService().startFavorStocksV3(stockId, request);
                return BaseModel.success(res);
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/wxMarketingStock")
    public BaseModel<String> createOrUpdateWxMarketingStock(HttpSession session, @RequestBody WxMarketingStock wxMarketingStock) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            wxMarketingStock.setWxAppId(user.getSessionWxAppId());
            int ret = wxMarketingStockService.createOrUpdate(wxMarketingStock);
            if (ret > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/wxMarketingStock")
    public BaseModel<WxMarketingStock> getWxMarketingStockByStockId(HttpSession session,
                                                                    @RequestParam("stockId") String stockId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            WxMarketingStock wxMarketingStock = wxMarketingStockService.queryByStockId(stockId);
            if (wxMarketingStock != null) {
                return BaseModel.success(wxMarketingStock);
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    public FavorStocksPauseResult pauseFavorStocksV3(String curMchId, String stockId, FavorStocksSetRequest request) throws WxPayException {
        String url = String.format("%s/v3/marketing/favor/stocks/%s/pause", wxPayService.switchoverTo(curMchId).getPayBaseUrl(), stockId);
        RsaCryptoUtil.encryptFields(request, wxPayService.getConfig().getVerifier().getValidCertificate());
        String result = wxPayService.postV3WithWechatpaySerial(url, GSON.toJson(request));
        return GSON.fromJson(result, FavorStocksPauseResult.class);
    }

    public FavorStocksRestartResult restartFavorStocksV3(String curMchId, String stockId, FavorStocksSetRequest request) throws WxPayException {
        String url = String.format("%s/v3/marketing/favor/stocks/%s/restart", wxPayService.switchoverTo(curMchId).getPayBaseUrl(), stockId);
        RsaCryptoUtil.encryptFields(request, wxPayService.getConfig().getVerifier().getValidCertificate());
        String result = wxPayService.postV3WithWechatpaySerial(url, GSON.toJson(request));
        return GSON.fromJson(result, FavorStocksRestartResult.class);
    }

    @PostMapping("/pauseStock")
    public BaseModel<FavorStocksPauseResult> pauseStock(HttpSession session, @RequestParam("stockId") String stockId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            try {
                FavorStocksSetRequest request = new FavorStocksSetRequest();
                request.setStockCreatorMchid(curMchId);
                FavorStocksPauseResult res = pauseFavorStocksV3(curMchId, stockId, request);
                log.info(res.toString());
                return BaseModel.success(res);
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/restartStock")
    public BaseModel<FavorStocksRestartResult> restartStock(HttpSession session, @RequestParam("stockId") String stockId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            try {
                FavorStocksSetRequest request = new FavorStocksSetRequest();
                request.setStockCreatorMchid(curMchId);
                FavorStocksRestartResult res = restartFavorStocksV3(curMchId, stockId, request);
                return BaseModel.success(res);
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @NoArgsConstructor
    @Data
    public static class FavorStocksPauseResult implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 生效时间
         * <p>
         * 生效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示，北京时间2015年5月20日 13点29分35秒。
         * 示例值：2015-05-20T13:29:35.120+08:00
         */
        @SerializedName("pause_time")
        private String pauseTime;

        /**
         * 批次号
         * <p>
         * 微信为每个代金券批次分配的唯一ID。
         * 示例值：98065001
         */
        @SerializedName("stock_id")
        private String stockId;
    }

    @NoArgsConstructor
    @Data
    public static class FavorStocksRestartResult implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 生效时间
         * <p>
         * 生效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示，北京时间2015年5月20日 13点29分35秒。
         * 示例值：2015-05-20T13:29:35.120+08:00
         */
        @SerializedName("restart_time")
        private String restartTime;

        /**
         * 批次号
         * <p>
         * 微信为每个代金券批次分配的唯一ID。
         * 示例值：98065001
         */
        @SerializedName("stock_id")
        private String stockId;
    }
}
