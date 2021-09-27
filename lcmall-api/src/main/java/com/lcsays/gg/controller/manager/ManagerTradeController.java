package com.lcsays.gg.controller.manager;

import com.kuaidi100.sdk.response.QueryTrackData;
import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.ec.*;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.ec.ExpressService;
import com.lcsays.gg.service.ec.OrderService;
import com.lcsays.gg.utils.ApiUtils;
import com.lcsays.gg.utils.SessionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-30 10:27
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/manager/trade")
public class ManagerTradeController {

    @Resource
    OrderService orderService;

    @Resource
    ExpressService expressService;

    private List<ExpressType> expressTypes;

    @PostConstruct
    private void init() {
        this.expressTypes = expressService.getAllExpressTypes();
    }

    @GetMapping("/order")
    public BaseModel<List<Order>> getOrders(HttpSession session,
                                               @RequestParam("current") Integer current,
                                               @RequestParam("pageSize") Integer pageSize,
                                               String outTradeNo) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            List<Order> orders = orderService.getOrdersByAppId(user.getSessionWxApp(), outTradeNo);
            return BaseModel.success(ApiUtils.pagination(orders, current, pageSize), orders.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/order")
    public BaseModel<Long> updateOrder(HttpSession session, @RequestBody Order order) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            int ret = orderService.updateOrder(order);
            if (ret > 0) {
            log.info(order.toString());
                return BaseModel.success(order.getId());
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/expressTypes")
    public BaseModel<List<ExpressType>> getExpressTypes() {
        return BaseModel.success(this.expressTypes);
    }

    @GetMapping("/expressTrack")
    public BaseModel<List<QueryTrackData>> expressTrack(@RequestParam String outTradeNo) {
        Order order = orderService.getOrderByOutTradeNo(outTradeNo);
        if (null == order) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }

        List<QueryTrackData> data = expressService.expressTrack(
            order.getExpressType().getCode(),
            order.getExpressNo(),
            order.getAddress().getPhone()
        );
        if (null != data) {
            return BaseModel.success(data);
        }
        return BaseModel.error(ErrorCode.NO_RESULT);
    }
}
