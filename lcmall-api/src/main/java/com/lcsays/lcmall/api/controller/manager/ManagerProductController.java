package com.lcsays.lcmall.api.controller.manager;

import com.lcsays.lcmall.api.enums.ErrorCode;
import com.lcsays.lcmall.api.enums.ec.PricePolicy;
import com.lcsays.lcmall.api.models.dbwrapper.EcProductEx;
import com.lcsays.lcmall.api.models.dbwrapper.EcSkuEx;
import com.lcsays.lcmall.api.models.result.BaseModel;
import com.lcsays.lcmall.api.utils.ApiUtils;
import com.lcsays.lcmall.api.utils.SessionUtils;
import com.lcsays.lcmall.db.model.*;
import com.lcsays.lcmall.db.service.*;
import com.lcsays.lcmall.db.util.WxMaUserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: lichuang
 * @Date: 21-8-30 10:27
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/manager/product")
public class ManagerProductController {

    @Resource
    EcCategoryService ecCategoryService;

    @Resource
    EcProductService ecProductService;

    @Resource
    EcSkuService ecSkuService;

    @Resource
    EcPriceService ecPriceService;

    @Resource
    private WxAppService wxAppService;

    @GetMapping("/category")
    public BaseModel<List<EcCategory>> category(HttpSession session) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            List<EcCategory> categories = ecCategoryService.queryByWxAppId(user.getSessionWxAppId());
            return BaseModel.success(categories);
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/category")
    public BaseModel<Integer> addCategory(HttpSession session, @RequestBody EcCategory category) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            EcCategory c = new EcCategory();
            c.setWxAppId(user.getSessionWxAppId());
            c.setName(category.getName());
            if (ecCategoryService.insert(c) > 0) {
                return BaseModel.success(c.getId());
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/category")
    public BaseModel<String> updateCategory(HttpSession session, @RequestBody EcCategory category) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            EcCategory c = ecCategoryService.queryById(category.getId());
            if (null != c) {
                c.setName(category.getName());
                if (ecCategoryService.update(c) > 0) {
                    return BaseModel.success();
                } else {
                    return BaseModel.error(ErrorCode.DAO_ERROR);
                }
            } else {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @DeleteMapping("/category")
    public BaseModel<String> deleteCategory(HttpSession session, @RequestBody EcCategory category) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            List<EcProduct> products = ecProductService.queryByCategoryId(category.getId());
            if (null != products && products.size() > 0) {
                return BaseModel.error(ErrorCode.DELETE_FORBIDDEN);
            } else {
                ecCategoryService.deleteById(category.getId());
                return BaseModel.success();
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/product")
    public BaseModel<List<EcProductEx>> getProduct(HttpSession session,
                                                   @RequestParam("current") Integer current,
                                                   @RequestParam("pageSize") Integer pageSize,
                                                   String name,
                                                   Integer categoryId) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            List<EcCategory> categories = ecCategoryService.queryByWxAppId(user.getSessionWxAppId());
            List<Integer> categoryIds = new ArrayList<>();
            Map<Integer, EcCategory> catMap = new HashMap<>();
            for (EcCategory c: categories) {
                categoryIds.add(c.getId());
                catMap.put(c.getId(), c);
            }
            List<EcProduct> products = ecProductService.queryBy(categoryIds, name, categoryId);
            List<EcProductEx> data = new ArrayList<>();
            for (EcProduct p: products) {
                EcProductEx ecProductEx = new EcProductEx();
                ecProductEx.copyFrom(p);
                EcCategory c = catMap.get(p.getCategoryId());
                ecProductEx.setCategory(c);
            }
            return BaseModel.success(ApiUtils.pagination(data, current, pageSize), data.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/product")
    public BaseModel<Integer> addProduct(HttpSession session, @RequestBody EcProduct product) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            int ret = ecProductService.insert(product);
            if (ret > 0) {
                return BaseModel.success(product.getId());
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/product")
    public BaseModel<Integer> updateProduct(HttpSession session, @RequestBody EcProduct product) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            log.info(product.toString());
            int ret = ecProductService.update(product);
            if (ret > 0) {
                return BaseModel.success(product.getId());
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @DeleteMapping("/product")
    public BaseModel<String> deleteProduct(HttpSession session, @RequestBody EcProduct product) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            log.info(product.toString());
            int ret = ecProductService.deleteById(product.getId());
            if (ret > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/sku")
    public BaseModel<List<EcSku>> getSku(HttpSession session,
                                         @RequestParam("current") Integer current,
                                         @RequestParam("pageSize") Integer pageSize,
                                         String name) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            List<EcCategory> categories = ecCategoryService.queryByWxAppId(user.getSessionWxAppId());
            List<Integer> categoryIds = new ArrayList<>();
            for (EcCategory c: categories) {
                categoryIds.add(c.getId());
            }
            List<EcProduct> products = ecProductService.queryBy(categoryIds, null, null);
            List<Integer> productIds = new ArrayList<>();
            for (EcProduct p: products) {
                productIds.add(p.getId());
            }
            List<EcSku> skus = ecSkuService.queryBy(productIds, name);
            return BaseModel.success(ApiUtils.pagination(skus, current, pageSize), skus.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/sku")
    public BaseModel<Integer> addSku(HttpSession session, @RequestBody EcSkuEx sku) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }

            EcSku ecSku = new EcSku();
            sku.copyTo(ecSku);
            int ret = ecSkuService.insert(ecSku);
            if (ret > 0) {
                EcPrice ecPrice = new EcPrice();
                ecPrice.setPrice(sku.getPriceValue());
                ecPrice.setPolicy(PricePolicy.PP_RAW.getValue());
                ecPrice.setSkuId(ecSku.getId());
                int priceRet = ecPriceService.insert(ecPrice);
                if (priceRet > 0) {
                    return BaseModel.success(ecSku.getId());
                } else {
                    return BaseModel.error(ErrorCode.DAO_ERROR);
                }
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/sku")
    public BaseModel<Integer> updateSku(HttpSession session, @RequestBody EcSkuEx sku) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            EcPrice ecPrice = ecPriceService.queryBySkuId(sku.getId());
            ecPrice.setPrice(sku.getPriceValue());
            int ret1 = ecPriceService.update(ecPrice);
            log.info(sku.toString());
            EcSku ecSku = ecSkuService.queryById(sku.getId());
            if (null == ecSku) {
                return BaseModel.error(ErrorCode.NO_RESULT);
            }
            sku.copyTo(ecSku);
            int ret2 = ecSkuService.update(ecSku);
            if (ret1 > 0 && ret2 > 0) {
                return BaseModel.success(sku.getId());
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @DeleteMapping("/sku")
    public BaseModel<String> deleteSku(HttpSession session, @RequestBody EcSku sku) {
        WxMaUser user = SessionUtils.getWxUserFromSession(session);
        if (null != user) {
            if (!WxMaUserUtil.checkAuthority(user, wxAppService)) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            log.info(sku.toString());
            int ret = ecSkuService.deleteById(sku.getId());
            if (ret > 0) {
                return BaseModel.success();
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }
}
