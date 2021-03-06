package com.lcsays.lcmall.api.controller.manager;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.binarywang.wxpay.bean.marketing.*;
import com.github.binarywang.wxpay.bean.marketing.enums.StockTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.RsaCryptoUtil;
import com.google.gson.annotations.SerializedName;
import com.lcsays.lcmall.api.config.ManagerConfiguration;
import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.manager.CouponStatistics;
import com.lcsays.lcmall.api.models.manager.TrendStatistics;
import com.lcsays.lcmall.api.models.manager.FlowStatistics;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.*;
import com.lcsays.lcmall.db.model.*;
import com.lcsays.lcmall.db.service.*;
import com.lcsays.lcmall.db.util.WxMaUserUtil;
import com.lcsays.lcmall.db.util.WxMarketingActivityUtil;
import com.lcsays.lcmall.db.util.WxTrackUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

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

    @Resource
    WxMarketingActivityService wxMarketingActivityService;

    @Resource
    WxTrackService wxTrackService;

    @Resource
    ManagerConfiguration managerConfiguration;

    @GetMapping("/whitelist")
    public BaseModel<List<WxMarketingWhitelist>> whitelist(HttpSession session,
                                                           @RequestParam("current") Integer current,
                                                           @RequestParam("pageSize") Integer pageSize,
                                                           @RequestParam Integer activityId,
                                                           String phoneNumber) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (WxMaUserUtil.checkAuthority(user, wxAppService)) {

                WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
                if (null == activity) {
                    return BaseModel.error(ErrorCode.PARAM_ERROR);
                }

                List<WxMarketingWhitelist> data;
                if (!StringUtils.isEmpty(phoneNumber)) {
                    try {
                        String userPhoneEncrypt = SensitiveInfoUtils.encrypt(phoneNumber, managerConfiguration.getSensitiveSalt(), managerConfiguration.getSensitiveKey());
                        data = wxMarketingWhitelistService.queryByBatchNoAndUserPhoneEncrypt(activity.getWhitelistBatchNo(), userPhoneEncrypt);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("encrypt error: " + phoneNumber + " " + e.getMessage());
                        return BaseModel.error(ErrorCode.ENCRYPT_ERROR);
                    }
                } else {
                    data = new ArrayList<>();
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
                request.setAvailableBeginTime(TimeUtils.timeStr2Rfc3399(request.getAvailableBeginTime()));
                request.setAvailableEndTime(TimeUtils.timeStr2Rfc3399(request.getAvailableEndTime()));
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

    private int createOrUpdateMarketingStock(WxMarketingStock wxMarketingStock) throws WxPayException {
        // ??????????????????
        String curMchId = wxAppService.queryById(wxMarketingStock.getWxAppId()).getMchId();
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

            try {
                int ret = createOrUpdateMarketingStock(wxMarketingStock);
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

    @GetMapping("/activity")
    public BaseModel<List<WxMarketingActivity>> getActivity(HttpSession session,
                                                                @RequestParam("current") Integer current,
                                                                @RequestParam("pageSize") Integer pageSize) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            List<WxMarketingActivity> activities = wxMarketingActivityService.queryByWxAppId(user.getSessionWxAppId());
            return BaseModel.success(activities);
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/activity")
    public BaseModel<String> updateActivity(HttpSession session, @RequestBody WxMarketingActivity activity) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            if (wxMarketingActivityService.update(activity) > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
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
    public BaseModel<List<CouponStatistics>> couponStatistics(HttpSession session, @RequestParam Integer activityId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
            if (null == activity) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }

            // ?????????????????????????????????
            WxApp wxApp = WxMaUserUtil.getSessionWxApp(user, wxAppService);
            Integer curWxAppId = wxApp.getId();

            // ??????map(??????,??????)???
            Map<String, WxMaUser> usersWithUserPhoneEncryptMap = new HashMap<>();
            for (WxMaUser u: wxMaUserService.listUsersWithPhoneNumberByWxAppId(curWxAppId)) {
                usersWithUserPhoneEncryptMap.put(u.getUserPhoneEncrypt(), u);
            }

            // ??????map(stockId, Stock)???
            Map<String, WxMarketingStock> stocksMap = wxMarketingStockService.getStocksMapByWxAppId(curWxAppId);
            for (WxMarketingStock stock: stocksMap.values()) {
                if (stock.getStockName() == null) {
                    try {
                        createOrUpdateMarketingStock(stock);
                    } catch (WxPayException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }

            String[] stockIds = WxMarketingActivityUtil.getStockIds(activity);

            // ??????map(??????id,coupon)???
            Map<Integer, CouponStatistics.CouponsInfo> userCouponsInfoMap = new HashMap<>();
            for (WxMarketingCoupon coupon: wxMarketingCouponService.queryByStockIds(stockIds)) {
                Integer wxMaUserId = coupon.getWxMaUserId();
                if (userCouponsInfoMap.containsKey(wxMaUserId)) {
                    CouponStatistics.CouponsInfo couponsInfo = userCouponsInfoMap.get(wxMaUserId);
                    List<CouponStatistics.CouponItem> coupons = couponsInfo.getCoupons();
                    // ??????CouponItem
                    CouponStatistics.CouponItem item = new CouponStatistics.CouponItem();
                    item.setStockId(coupon.getStockId());
                    item.setCouponId(coupon.getCouponId());
                    item.setStatus(coupon.getStatus());
                    if (stocksMap.containsKey(coupon.getStockId())) {
                        WxMarketingStock stock = stocksMap.get(coupon.getStockId());
                        item.setTransactionMinimum(stock.getTransactionMinimum());
                        item.setCouponAmount(stock.getCouponAmount());
                        if ("USED".equals(coupon.getStatus())) {
                            item.setConsumeMchid(coupon.getConsumeMchid());
                            item.setConsumeTime(coupon.getConsumeTime());
                            item.setTransactionId(coupon.getTransactionId());
                            couponsInfo.setConsumeCount(couponsInfo.getConsumeCount() + 1);
                            if (null != stock.getCouponAmount()) {
                                couponsInfo.setConsumeAmount(couponsInfo.getConsumeAmount() + stock.getCouponAmount());
                            } else {
                                log.warn("???stock?????????????????????stockId: " + stock.getStockId());
                            }
                        }
                    }
                    coupons.add(item);
                } else {
                    CouponStatistics.CouponsInfo couponsInfo = new CouponStatistics.CouponsInfo();
                    couponsInfo.setConsumeCount(0);
                    couponsInfo.setConsumeAmount(0);
                    List<CouponStatistics.CouponItem> coupons = new ArrayList<>();

                    // ??????CouponItem
                    CouponStatistics.CouponItem item = new CouponStatistics.CouponItem();
                    item.setStockId(coupon.getStockId());
                    item.setCouponId(coupon.getCouponId());
                    item.setStatus(coupon.getStatus());
                    if (stocksMap.containsKey(coupon.getStockId())) {
                        WxMarketingStock stock = stocksMap.get(coupon.getStockId());
                        item.setTransactionMinimum(stock.getTransactionMinimum());
                        item.setCouponAmount(stock.getCouponAmount());
                        if ("USED".equals(coupon.getStatus())) {
                            item.setConsumeMchid(coupon.getConsumeMchid());
                            item.setConsumeTime(coupon.getConsumeTime());
                            item.setTransactionId(coupon.getTransactionId());
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

            for (WxMarketingWhitelist whitelistItem:
                    wxMarketingWhitelistService.queryByBatchNoAndUserPhoneEncrypts(
                            activity.getWhitelistBatchNo(),
                            new ArrayList<>(usersWithUserPhoneEncryptMap.keySet())
                    )) {
                CouponStatistics cs = new CouponStatistics();
                String userPhoneEncrypt = whitelistItem.getUserPhoneEncrypt();

                String whitelistPhoneNumber = null;
                try {
                    whitelistPhoneNumber = SensitiveInfoUtils.decrypt(userPhoneEncrypt, managerConfiguration.getSensitiveSalt(), managerConfiguration.getSensitiveKey());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("decrypt error: " + whitelistItem.getId() + " " + e.getMessage());
                }
                cs.setWhitelistPhoneNumber(whitelistPhoneNumber);
                cs.setWxMaUserHasPhoneNumber(true);
                WxMaUser wxMaUser = usersWithUserPhoneEncryptMap.get(userPhoneEncrypt);
                cs.setCouponsInfo(userCouponsInfoMap.get(wxMaUser.getId()));
                ret.add(cs);
            }


            return BaseModel.success(ret);
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/flowStatistics")
    public BaseModel<FlowStatistics> flowStatistics(HttpSession session,
                                                          @RequestParam Integer activityId,
                                                          @RequestParam String dateStr) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
            if (null == activity) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }

            // ????????????pv
            Map<String, Integer> totalEventTypeCounter = new HashMap<>();
            Map<String, Integer> eventTypeCounter = new HashMap<>();
            // ????????????uv
            Map<String, Set<String>> totalEventTypeSet = new HashMap<>();
            Map<String, Set<String>> eventTypeSet = new HashMap<>();
            Date begin;
            Date end;
            try {
                begin = TimeUtils.timeStr2Date(dateStr + " 00:00:00");
                end = TimeUtils.timeStr2Date(dateStr + " 23:59:59");
            } catch (Exception e) {
                log.error("dateStr: " + dateStr);
                throw e;
            }
            for (WxTrack track: wxTrackService.queryByActivityId(activityId)) {
                // ????????????
                accumulateEventType(totalEventTypeCounter, totalEventTypeSet, track);

                // ????????????
                if (track.getCreateTime().after(begin) && track.getCreateTime().before(end)) {
                    accumulateEventType(eventTypeCounter, eventTypeSet, track);
                }
            }

            Set<String> stockIdSet = WxMarketingActivityUtil.getStockIdsSet(activity);
            String[] uniqStockIds = new String[stockIdSet.size()];
            stockIdSet.toArray(uniqStockIds);

            int totalCouponCount = 0;
            int couponCount = 0;
            int totalConsumeCount = 0;
            int consumeCount = 0;
            List<WxMarketingCoupon> coupons = wxMarketingCouponService.queryByStockIds(uniqStockIds);
            for (WxMarketingCoupon coupon: coupons) {
                totalCouponCount++;
                if ("USED".equals(coupon.getStatus())) {
                    totalConsumeCount++;
                }

                if (coupon.getCreateTime().after(begin) && coupon.getCreateTime().before(end)) {
                    couponCount++;
                }

                if (null != coupon.getConsumeTime() && coupon.getConsumeTime().after(begin) && coupon.getConsumeTime().before(end)) {
                    if ("USED".equals(coupon.getStatus())) {
                        consumeCount++;
                    }
                }
            }

            // ???????????????????????????????????????????????????
            FlowStatistics ret = new FlowStatistics();
            ret.setTotalPv(totalEventTypeCounter.get("stockIndexPageView"));
            if (null != totalEventTypeSet.get("stockIndexPageView")) {
                ret.setTotalUv(totalEventTypeSet.get("stockIndexPageView").size());
            }
            ret.setTotalClickPv(totalEventTypeCounter.get("getCouponInternal"));
            if (null != totalEventTypeSet.get("getCouponInternal")) {
                ret.setTotalClickUv(totalEventTypeSet.get("getCouponInternal").size());
            }
            ret.setPv(eventTypeCounter.get("stockIndexPageView"));
            if (null != eventTypeSet.get("stockIndexPageView")) {
                ret.setUv(eventTypeSet.get("stockIndexPageView").size());
            }
            ret.setClickPv(eventTypeCounter.get("getCouponInternal"));
            if (null != eventTypeSet.get("getCouponInternal")) {
                ret.setClickUv(eventTypeSet.get("getCouponInternal").size());
            }

            ret.setTotalCouponCount(totalCouponCount);
            ret.setTotalConsumeCount(totalConsumeCount);
            ret.setCouponCount(couponCount);
            ret.setConsumeCount(consumeCount);

            return BaseModel.success(ret);
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    private void accumulateEventType(Map<String, Integer> eventTypeCounter, Map<String, Set<String>> eventTypeSet, WxTrack track) {
        if (eventTypeCounter.containsKey(track.getEventType())) {
            eventTypeCounter.put(track.getEventType(), eventTypeCounter.get(track.getEventType()) + 1);
            eventTypeSet.get(track.getEventType()).add(track.getIp() + track.getUa());
        } else {
            eventTypeCounter.put(track.getEventType(), 1);

            Set<String> set = new HashSet<>();
            set.add(track.getIp() + track.getUa());
            eventTypeSet.put(track.getEventType(), set);
        }
    }

    /**
     * ?????????????????????
     * @param session
     * @param activityId
     * @return
     */
    @GetMapping("/trendStat")
    public BaseModel<TrendStatistics> trendStat(HttpSession session,
                                                @RequestParam Integer activityId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
            if (null == activity) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }

            String[] stockIds = WxMarketingActivityUtil.getStockIds(activity);
            String [] stockIds2 = {"15956311", "15956243", "15968938", "15974650", "15984657", "15989523"};
            List<WxMarketingCoupon> coupons = wxMarketingCouponService.queryByStockIds(stockIds);

            // ???????????????????????????
            Map<String, Integer> dateCounter = new TreeMap<>();
            Date begin = coupons.get(0).getCreateTime();
            Date end = coupons.get(coupons.size()-1).getCreateTime();
            Calendar c = Calendar.getInstance();
            for (Date cur = begin; cur.before(end); ) {
                String ymd = TimeUtils.date2YMD(cur);
                dateCounter.put(ymd, 0);
                c.setTime(cur);
                c.add(Calendar.DAY_OF_MONTH, 1);
                cur = c.getTime();
            }

            // ?????????????????????
            for (WxMarketingCoupon coupon: coupons) {
                if (coupon.getCreateTime() != null) {
                    String ymd = TimeUtils.date2YMD(coupon.getCreateTime());
                    if (dateCounter.containsKey(ymd)) {
                        dateCounter.put(ymd, dateCounter.get(ymd) + 1);
                    } else {
                        dateCounter.put(ymd, 1);
                    }
                }
            }

            List<String> keys = new ArrayList<>();
            List<Integer> values = new ArrayList<>();
            for (Map.Entry<String, Integer> entry: dateCounter.entrySet()) {
                keys.add(entry.getKey());
                values.add(entry.getValue());
            }

            TrendStatistics data = new TrendStatistics();
            data.setKeys(keys);
            data.setValues(values);

            return BaseModel.success(data);
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/downloadXls")
    public void downloadXls(HttpSession session,
                            HttpServletResponse response,
                            @RequestParam Integer activityId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return;
            }

            WxMarketingActivity activity = wxMarketingActivityService.queryById(activityId);
            if (null == activity) {
                return;
            }

            Set<String> stockIdSet = WxMarketingActivityUtil.getStockIdsSet(activity);
            String[] uniqStockIds = new String[stockIdSet.size()];
            stockIdSet.toArray(uniqStockIds);

            // ?????????????????????
            List<WxMarketingCoupon> coupons = wxMarketingCouponService.queryByStockIds(uniqStockIds);

            WxApp wxApp = WxMaUserUtil.getSessionWxApp(user, wxAppService);
            Integer curWxAppId = wxApp.getId();
            Map<Integer, WxMaUser> usersWithPhoneNumberMap = new HashMap<>();
            for (WxMaUser u: wxMaUserService.listUsersWithPhoneNumberByWxAppId(curWxAppId)) {
                usersWithPhoneNumberMap.put(u.getId(), u);
            }


            // ????????????????????????
            Map<Integer, WxMaUser> usersWithTracksMap = new HashMap<>();
            List<WxTrack> loginedTracks = wxTrackService.queryByActivityIdAndEventType(activityId, "stockIndexPageViewLogined");
            for (WxTrack track: loginedTracks) {
                if (usersWithPhoneNumberMap.containsKey(track.getWxMaUserId())) {
                    usersWithTracksMap.put(track.getWxMaUserId(), usersWithPhoneNumberMap.get(track.getWxMaUserId()));
                }
            }

            String fileName = "/tmp/" + activityId + ".xlsx";
            ExcelWriter ew = ExcelUtil.getWriter(fileName);
            List<Map<String, String>> list = new ArrayList<>();

            for (WxMarketingCoupon coupon: coupons) {
                Map<String, String> map = new HashMap<>();
                if (usersWithPhoneNumberMap.containsKey(coupon.getWxMaUserId())) {
                    map.put("????????????", usersWithPhoneNumberMap.get(coupon.getWxMaUserId()).getUserPhone());
                    map.put("??????????????????/??????", "??????");
                    map.put("????????????/??????", "??????");
                    map.put("??????????????????", "");
                    map.put("?????????", coupon.getStockId());
                    map.put("??????", coupon.getCouponId());
                    map.put("????????????", TimeUtils.date2Str(coupon.getCreateTime()));
                    if ("USED".equals(coupon.getStatus())) {
                        map.put("????????????", "?????????");
                        map.put("????????????", TimeUtils.date2Str(coupon.getConsumeTime()));
                        map.put("???????????????", coupon.getConsumeMchid());
                        map.put("???????????????", coupon.getTransactionId());
                    } else {
                        map.put("????????????", "?????????");
                        map.put("????????????", "");
                        map.put("???????????????", "");
                        map.put("???????????????", "");
                    }

                    if (usersWithTracksMap.containsKey(coupon.getWxMaUserId())) {
                        usersWithTracksMap.remove(coupon.getWxMaUserId());
                    }
                }
                list.add(map);
            }

            Set<String> whiteListUserPhoneSet = new HashSet<>();
            for (WxMarketingWhitelist whiteList: wxMarketingWhitelistService.queryByBatchNo(activity.getWhitelistBatchNo())) {
                whiteListUserPhoneSet.add(whiteList.getUserPhone());
            }


            // ??????????????????coupon??????track???
            for (Integer wxMaUserId: usersWithTracksMap.keySet()) {
                WxMaUser wxMaUser = usersWithTracksMap.get(wxMaUserId);
                Map<String, String> map = new HashMap<>();
                map.put("????????????", wxMaUser.getUserPhone());
                map.put("??????????????????/??????", "??????");
                map.put("????????????/??????", "??????");
                if (whiteListUserPhoneSet.contains(wxMaUser.getUserPhone())) {
                    map.put("??????????????????", "");
                } else {
                    map.put("??????????????????", "??????????????????");
                }
                map.put("?????????", "");
                map.put("??????", "");
                map.put("????????????", "");
                map.put("????????????", "");
                map.put("????????????", "");
                map.put("???????????????", "");
                map.put("???????????????", "");
                list.add(map);
            }

            ew.write(list);
            ew.close();

            File file = new File(fileName);
            InputStream in;
            try {
                in = new FileInputStream(file);
                int tempbyte;

                response.setContentType("application/force-download");
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try {
                    OutputStream outputStream = response.getOutputStream();
                    while ((tempbyte = in.read()) != -1) {
                        outputStream.write(tempbyte);
                    }
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            file.delete();
        }
    }

    @GetMapping("/userTracks")
    public BaseModel<List<WxTrack>> userTracks(HttpSession session, @RequestParam String phoneNumber) {
        log.info("userTracks");
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            try {
                String userPhoneEncrypt = SensitiveInfoUtils.encrypt(phoneNumber, managerConfiguration.getSensitiveSalt(), managerConfiguration.getSensitiveKey());
                WxMaUser wxMaUser = wxMaUserService.queryUserByWxAppIdAndUserPhoneEncrypt(user.getSessionWxAppId(), userPhoneEncrypt);

                if (null != wxMaUser) {
                    List<WxTrack> tracks = wxTrackService.queryByWxMaUserId(wxMaUser.getId());
                    for (WxTrack track: tracks) {
                        WxTrackUtil.normalizeTime(track);
                    }
                    return BaseModel.success(tracks);
                } else {
                    return BaseModel.error(ErrorCode.NO_RESULT);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("encrypt fail " + e.getMessage());
                return BaseModel.error(ErrorCode.ENCRYPT_ERROR);
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
         * ????????????
         * <p>
         * ?????????????????????rfc3339????????????????????????YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE???YYYY-MM-DD??????????????????T??????????????????????????????time??????????????????HH:mm:ss.sss????????????????????????TIMEZONE???????????????+08:00??????????????????????????????UTC 8???????????????????????????????????????2015-05-20T13:29:35.120+08:00?????????????????????2015???5???20??? 13???29???35??????
         * ????????????2015-05-20T13:29:35.120+08:00
         */
        @SerializedName("pause_time")
        private String pauseTime;

        /**
         * ?????????
         * <p>
         * ?????????????????????????????????????????????ID???
         * ????????????98065001
         */
        @SerializedName("stock_id")
        private String stockId;
    }

    @NoArgsConstructor
    @Data
    public static class FavorStocksRestartResult implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * ????????????
         * <p>
         * ?????????????????????rfc3339????????????????????????YYYY-MM-DDTHH:mm:ss.sss+TIMEZONE???YYYY-MM-DD??????????????????T??????????????????????????????time??????????????????HH:mm:ss.sss????????????????????????TIMEZONE???????????????+08:00??????????????????????????????UTC 8???????????????????????????????????????2015-05-20T13:29:35.120+08:00?????????????????????2015???5???20??? 13???29???35??????
         * ????????????2015-05-20T13:29:35.120+08:00
         */
        @SerializedName("restart_time")
        private String restartTime;

        /**
         * ?????????
         * <p>
         * ?????????????????????????????????????????????ID???
         * ????????????98065001
         */
        @SerializedName("stock_id")
        private String stockId;
    }
}
