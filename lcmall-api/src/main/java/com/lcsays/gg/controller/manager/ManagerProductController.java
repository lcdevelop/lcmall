package com.lcsays.gg.controller.manager;

import com.lcsays.gg.enums.ErrorCode;
import com.lcsays.gg.models.ec.Category;
import com.lcsays.gg.models.ec.Price;
import com.lcsays.gg.models.ec.Product;
import com.lcsays.gg.models.ec.Sku;
import com.lcsays.gg.models.result.BaseModel;
import com.lcsays.gg.models.weixin.WxMaUser;
import com.lcsays.gg.service.ec.SkuService;
import com.lcsays.gg.utils.ApiUtils;
import com.lcsays.gg.utils.SessionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/manager/product")
public class ManagerProductController {

    @Resource
    SkuService skuService;

    @GetMapping("/category")
    public BaseModel<List<Category>> category(HttpSession session) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            List<Category> categories = skuService.categoryList(user.getSessionWxApp());
            return BaseModel.success(categories);
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/category")
    public BaseModel<Long> addCategory(HttpSession session, @RequestBody Category category) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            Category c = new Category(user.getSessionWxApp(), category.getName());
            if (skuService.createCategory(c) > 0) {
                return BaseModel.success(c.getId());
            } else {
                return BaseModel.error(ErrorCode.DAO_ERROR);
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PutMapping("/category")
    public BaseModel<String> updateCategory(HttpSession session, @RequestBody Category category) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            Category c = skuService.getCategoryById(category.getId());
            if (null != c) {
                c.setName(category.getName());
                if (skuService.updateCategory(category) > 0) {
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
    public BaseModel<String> deleteCategory(HttpSession session, @RequestBody Category category) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            List<Product> products = skuService.getProductsByCategoryId(category.getId());
            if (null != products && products.size() > 0) {
                return BaseModel.error(ErrorCode.DELETE_FORBIDDEN);
            } else {
                skuService.deleteCategoryById(category.getId());
                return BaseModel.success();
            }
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @GetMapping("/product")
    public BaseModel<List<Product>> getProduct(HttpSession session,
                                               @RequestParam("current") Integer current,
                                               @RequestParam("pageSize") Integer pageSize,
                                               String name,
                                               Integer categoryId) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            List<Product> products = skuService.getProducts(user.getSessionWxApp(), name, categoryId);
            return BaseModel.success(ApiUtils.pagination(products, current, pageSize), products.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/product")
    public BaseModel<Long> addProduct(HttpSession session, @RequestBody Product product) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            int ret = skuService.createProduct(product);
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
    public BaseModel<Long> updateProduct(HttpSession session, @RequestBody Product product) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            log.info(product.toString());
            int ret = skuService.updateProduct(product);
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
    public BaseModel<String> deleteProduct(HttpSession session, @RequestBody Product product) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            log.info(product.toString());
            int ret = skuService.deleteProductById(product.getId());
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
    public BaseModel<List<Sku>> getSku(HttpSession session,
                                       @RequestParam("current") Integer current,
                                       @RequestParam("pageSize") Integer pageSize,
                                       String name) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            List<Sku> skus = skuService.getSkusByAppId(user.getSessionWxApp(), name);
            return BaseModel.success(ApiUtils.pagination(skus, current, pageSize), skus.size());
        } else {
            return BaseModel.error(ErrorCode.NEED_LOGIN);
        }
    }

    @PostMapping("/sku")
    public BaseModel<Long> addSku(HttpSession session, @RequestBody Sku sku) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            Price price = sku.getPrice();
            log.info(price.toString());
            log.info(sku.toString());
            int ret = skuService.createSku(sku);
            if (ret > 0) {
                price.setSkuId(sku.getId());
                int priceRet = skuService.createPrice(price);
                if (priceRet > 0) {
                    return BaseModel.success(sku.getId());
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
    public BaseModel<Long> updateSku(HttpSession session, @RequestBody Sku sku) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            Price price = sku.getPrice();
            int ret1 = skuService.updatePrice(price);
            log.info(sku.toString());
            int ret2 = skuService.updateSku(sku);
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
    public BaseModel<String> deleteSku(HttpSession session, @RequestBody Sku sku) {
        WxMaUser user = SessionUtils.getUserFromSession(session);
        if (null != user) {
            if (!user.checkAuthority()) {
                return BaseModel.error(ErrorCode.NO_AUTHORITY);
            }
            log.info(sku.toString());
            int ret = skuService.deleteSkuById(sku.getId());
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
