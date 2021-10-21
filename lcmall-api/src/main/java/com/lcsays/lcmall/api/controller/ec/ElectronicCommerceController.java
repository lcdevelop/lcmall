package com.lcsays.lcmall.api.controller.ec;

import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.models.ec.Cart;
import com.lcsays.lcmall.api.models.ec.Category;
import com.lcsays.lcmall.api.models.ec.Sku;
import com.lcsays.lcmall.api.models.manager.WxApp;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.models.weixin.WxMaUser;
import com.lcsays.lcmall.api.service.ec.SkuService;
import com.lcsays.lcmall.api.service.manager.AppService;
import com.lcsays.lcmall.api.service.weixin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: lichuang
 * @Date: 21-8-11 22:01
 * 注解@RequestBody对应客户端要用postJson，@RequestParam对应客户端要用post/fetch
 */
@Slf4j
@RestController
@RequestMapping("/api/ec/{appId}")
public class ElectronicCommerceController {

    @Resource
    AppService appService;

    @Resource
    UserService userService;

    @Resource
    SkuService skuService;

    @GetMapping("/category_list")
    public BaseModel<List<Category>> categoryList(@PathVariable String appId) {
        WxApp wxApp = appService.getWxAppByAppId(appId);
        if (null == wxApp) {
            return BaseModel.error(ErrorCode.PARAM_ERROR);
        }
        List<Category> categorys = skuService.categoryList(wxApp);
        return BaseModel.success(categorys);
    }

    @GetMapping("/sku_list")
    public BaseModel<List<Sku>> skuList(@PathVariable String appId, Long categoryId) {
        List<Sku> skuList;
        if (null == categoryId) {
            WxApp wxApp = appService.getWxAppByAppId(appId);
            if (null == wxApp) {
                return BaseModel.error(ErrorCode.PARAM_ERROR);
            }
            skuList = skuService.getSkusByAppId(wxApp, null);
        } else {
            skuList = skuService.getSkusByCategoryId(categoryId);
        }

        return BaseModel.success(skuList);
    }

    @GetMapping("/add_to_cart")
    public BaseModel<Integer> addToCart(@PathVariable String appId,
                                        @RequestParam("userId") Long userId,
                                        @RequestParam("skuId") Long skuId) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            int ret = skuService.addToCart(wxMaUser, skuId);
            return BaseModel.success(ret);
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/remove_from_cart")
    public BaseModel<Integer> removeFromCart(@PathVariable String appId,
                                        @RequestParam("userId") Long userId,
                                        @RequestParam("skuId") Long skuId) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            try {
                Integer ret = skuService.removeFromCart(wxMaUser, skuId);
                return BaseModel.success(ret);
            } catch (Exception e) {
                e.printStackTrace();
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/get_count_in_cart")
    public BaseModel<Integer> getCountInCart(@PathVariable String appId,
                                             @RequestParam("userId") Long userId) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            Integer ret = skuService.getCountInCart(wxMaUser);
            return BaseModel.success(ret);
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

    @GetMapping("/get_carts")
    public BaseModel<List<Cart>> getCarts(@PathVariable String appId,
                                          @RequestParam("userId") Long userId) {
        WxMaUser wxMaUser = userService.getWxMaUserById(appId, userId);
        if (null != wxMaUser) {
            List<Cart> carts = skuService.getCartsByUser(wxMaUser);
            return BaseModel.success(carts);
        } else {
            return BaseModel.error(ErrorCode.NO_USER);
        }
    }

}
