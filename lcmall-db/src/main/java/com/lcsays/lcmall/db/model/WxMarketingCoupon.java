package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxMarketingCoupon implements Serializable {
    private Integer id;

    private Integer wxAppId;

    private Integer wxMaUserId;

    private String stockId;

    private String couponId;

    private String status;

    private Date createTime;

    private Date consumeTime;

    private String consumeMchid;

    private String transactionId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(Integer wxAppId) {
        this.wxAppId = wxAppId;
    }

    public Integer getWxMaUserId() {
        return wxMaUserId;
    }

    public void setWxMaUserId(Integer wxMaUserId) {
        this.wxMaUserId = wxMaUserId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Date consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getConsumeMchid() {
        return consumeMchid;
    }

    public void setConsumeMchid(String consumeMchid) {
        this.consumeMchid = consumeMchid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxAppId=").append(wxAppId);
        sb.append(", wxMaUserId=").append(wxMaUserId);
        sb.append(", stockId=").append(stockId);
        sb.append(", couponId=").append(couponId);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", consumeTime=").append(consumeTime);
        sb.append(", consumeMchid=").append(consumeMchid);
        sb.append(", transactionId=").append(transactionId);
        sb.append("]");
        return sb.toString();
    }
}