package com.lcsays.gg.controller.manager;

import com.github.binarywang.wxpay.bean.marketing.*;
import com.github.binarywang.wxpay.bean.marketing.enums.StockTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.RsaCryptoUtil;
import com.google.gson.annotations.SerializedName;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.manager.CouponStatistics;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.utils.ApiUtils;
import com.lcsays.gg.utils.RequestNo;
import com.lcsays.gg.utils.SessionUtils;
import com.lcsays.gg.utils.TimeUtils;
import com.lcsays.lcmall.db.model.*;
import com.lcsays.lcmall.db.service.*;
import com.lcsays.lcmall.db.util.WxMaUserUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    WxMarketingConfigService wxMarketingConfigService;

    @Resource
    WxMaUserService wxMaUserService;

    @Resource
    WxMarketingCouponService wxMarketingCouponService;

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

    private int createOrUpdateMarketingStock(WxMarketingStock wxMarketingStock, String curMchId) throws WxPayException {
        // 查询批次详情
        FavorStocksGetResult result = wxPayService.switchoverTo(curMchId)
                .getMarketingFavorService().getFavorStocksV3(wxMarketingStock.getStockId(), curMchId);
        String createTime = result.getCreate_time();
        String availableBeginTime = result.getAvailableBeginTime();
        String availableEndTime = result.getAvailableEndTime();
        String description = result.getDescription();
        String stockName = result.getStockName();
        Integer transactionMinimum = result.getStockUseRule().getFixedNormalCoupon().getTransactionMinimum();
        Integer couponAmount = result.getStockUseRule().getFixedNormalCoupon().getCouponAmount();

        wxMarketingStock.setCreateTime(TimeUtils.rfc33992Date(createTime));
        wxMarketingStock.setAvailableBeginTime(TimeUtils.rfc33992Date(availableBeginTime));
        wxMarketingStock.setAvailableEndTime(TimeUtils.rfc33992Date(availableEndTime));
        wxMarketingStock.setDescription(description);
        wxMarketingStock.setStockName(stockName);
        wxMarketingStock.setTransactionMinimum(transactionMinimum);
        wxMarketingStock.setCouponAmount(couponAmount);

        return wxMarketingStockService.createOrUpdate(wxMarketingStock);
    }

    @PutMapping("/wxMarketingStock")
    public BaseModel<String> createOrUpdateWxMarketingStock(HttpSession session, @RequestBody WxMarketingStock wxMarketingStock) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            wxMarketingStock.setWxAppId(user.getSessionWxAppId());

            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();

            try {
                int ret = createOrUpdateMarketingStock(wxMarketingStock, curMchId);
                if (ret > 0) {
                    return BaseModel.success();
                } else {
                    return BaseModel.error(ErrorCode.DAO_ERROR);
                }
            } catch (WxPayException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return BaseModel.errorWithMsg(ErrorCode.WX_SERVICE_ERROR, e.getMessage());
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

    @PostMapping("/setCallbacks")
    public BaseModel<FavorCallbacksSaveResult> setCallbacks(HttpSession session, String notifyUrl) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            try {
                FavorCallbacksSaveRequest request = new FavorCallbacksSaveRequest();
                request.setMchid(curMchId);
                request.setNotifyUrl(notifyUrl);
                request.setSwitchBool(true);
                FavorCallbacksSaveResult res = wxPayService.switchoverTo(curMchId)
                        .getMarketingFavorService().saveFavorCallbacksV3(request);

                WxMarketingConfig wxMarketingConfig = new WxMarketingConfig();
                wxMarketingConfig.setWxAppId(user.getSessionWxAppId());
                wxMarketingConfig.setCallbacks(notifyUrl);
                wxMarketingConfigService.createOrUpdate(wxMarketingConfig);
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


    @GetMapping("/getCallbacks")
    public BaseModel<String> getCallbacks(HttpSession session) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            WxMarketingConfig config = wxMarketingConfigService.query(user.getSessionWxAppId());
            if (null != config) {
                return BaseModel.success(config.getCallbacks());
            } else {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/couponStatistics")
    public BaseModel<List<CouponStatistics>> couponStatistics(HttpSession session) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            // 加载map(电话,用户)表
            Map<String, WxMaUser> usersWithPhoneNumberMap = new HashMap<>();
            for (WxMaUser u: wxMaUserService.listUsersWithPhoneNumber()) {
                usersWithPhoneNumberMap.put(u.getPhoneNumber(), u);
            }

            // 加载map(stockId, Stock)表
            Map<String, WxMarketingStock> stocksMap = wxMarketingStockService.getStocksMap();
            String curMchId = WxMaUserUtil.getSessionWxApp(user, wxAppService).getMchId();
            for (WxMarketingStock stock: stocksMap.values()) {
                if (stock.getStockName() == null) {
                    try {
                        createOrUpdateMarketingStock(stock, curMchId);
                    } catch (WxPayException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }

            // 加载map(用户id,coupon)表
            Map<Integer, CouponStatistics.CouponsInfo> userCouponsInfoMap = new HashMap<>();
            for (WxMarketingCoupon coupon: wxMarketingCouponService.queryAll()) {
                Integer wxMaUserId = coupon.getWxMaUserId();
                if (userCouponsInfoMap.containsKey(wxMaUserId)) {
                    CouponStatistics.CouponsInfo couponsInfo = userCouponsInfoMap.get(wxMaUserId);
                    List<CouponStatistics.CouponItem> coupons = couponsInfo.getCoupons();
                    // 构造CouponItem
                    CouponStatistics.CouponItem item = new CouponStatistics.CouponItem();
                    item.setStockId(coupon.getStockId());
                    item.setCouponId(coupon.getCouponId());
                    item.setStatus(coupon.getStatus());
                    if (stocksMap.containsKey(coupon.getStockId())) {
                        WxMarketingStock stock = stocksMap.get(coupon.getStockId());
                        item.setTransactionMinimum(stock.getTransactionMinimum());
                        item.setCouponAmount(stock.getCouponAmount());
                        if ("USED".equals(coupon.getStatus())) {
                            couponsInfo.setConsumeCount(couponsInfo.getConsumeCount() + 1);
                            if (null != stock.getCouponAmount()) {
                                couponsInfo.setConsumeAmount(couponsInfo.getConsumeAmount() + stock.getCouponAmount());
                            } else {
                                log.warn("某stock没有详情数据，stockId: " + stock.getStockId());
                            }
                        }
                    }
                    coupons.add(item);
                } else {
                    CouponStatistics.CouponsInfo couponsInfo = new CouponStatistics.CouponsInfo();
                    couponsInfo.setConsumeCount(0);
                    couponsInfo.setConsumeAmount(0);
                    List<CouponStatistics.CouponItem> coupons = new ArrayList<>();

                    // 构造CouponItem
                    CouponStatistics.CouponItem item = new CouponStatistics.CouponItem();
                    item.setStockId(coupon.getStockId());
                    item.setCouponId(coupon.getCouponId());
                    item.setStatus(coupon.getStatus());
                    if (stocksMap.containsKey(coupon.getStockId())) {
                        WxMarketingStock stock = stocksMap.get(coupon.getStockId());
                        item.setTransactionMinimum(stock.getTransactionMinimum());
                        item.setCouponAmount(stock.getCouponAmount());
                        if ("USED".equals(coupon.getStatus())) {
                            couponsInfo.setConsumeCount(1);
                            couponsInfo.setConsumeAmount(stock.getCouponAmount());
                        }
                    }
                    coupons.add(item);
                    couponsInfo.setCoupons(coupons);

                    userCouponsInfoMap.put(coupon.getWxMaUserId(), couponsInfo);
                }
            }

            List<CouponStatistics> ret = new ArrayList<>();
            for (WxMarketingWhitelist whitelistItem: wxMarketingWhitelistService.queryAll()) {
                CouponStatistics cs = new CouponStatistics();
                String whitelistPhoneNumber = whitelistItem.getPhoneNumber();
                cs.setWhitelistPhoneNumber(whitelistPhoneNumber);
                if (usersWithPhoneNumberMap.containsKey(whitelistPhoneNumber)) {
                    cs.setWxMaUserHasPhoneNumber(true);
                    WxMaUser wxMaUser = usersWithPhoneNumberMap.get(whitelistPhoneNumber);
                    cs.setCouponsInfo(userCouponsInfoMap.get(wxMaUser.getId()));
                }
                ret.add(cs);
            }
            return BaseModel.success(ret);
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
