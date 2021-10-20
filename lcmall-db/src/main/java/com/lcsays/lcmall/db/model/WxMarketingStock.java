package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxMarketingStock implements Serializable {
    private Integer id;

    private Integer wxAppId;

    private String stockId;

    private String stockName;

    private String description;

    private Date createTime;

    private Date availableBeginTime;

    private Date availableEndTime;

    private Integer transactionMinimum;

    private Integer couponAmount;

    private String cardId;

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

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAvailableBeginTime() {
        return availableBeginTime;
    }

    public void setAvailableBeginTime(Date availableBeginTime) {
        this.availableBeginTime = availableBeginTime;
    }

    public Date getAvailableEndTime() {
        return availableEndTime;
    }

    public void setAvailableEndTime(Date availableEndTime) {
        this.availableEndTime = availableEndTime;
    }

    public Integer getTransactionMinimum() {
        return transactionMinimum;
    }

    public void setTransactionMinimum(Integer transactionMinimum) {
        this.transactionMinimum = transactionMinimum;
    }

    public Integer getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Integer couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxAppId=").append(wxAppId);
        sb.append(", stockId=").append(stockId);
        sb.append(", stockName=").append(stockName);
        sb.append(", description=").append(description);
        sb.append(", createTime=").append(createTime);
        sb.append(", availableBeginTime=").append(availableBeginTime);
        sb.append(", availableEndTime=").append(availableEndTime);
        sb.append(", transactionMinimum=").append(transactionMinimum);
        sb.append(", couponAmount=").append(couponAmount);
        sb.append(", cardId=").append(cardId);
        sb.append("]");
        return sb.toString();
    }
}