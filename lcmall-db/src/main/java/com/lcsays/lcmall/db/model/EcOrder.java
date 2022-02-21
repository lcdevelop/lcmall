package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class EcOrder implements Serializable {
    public static final Integer IS_DELETED = IsDel.IS_DELETED.value();

    public static final Integer NOT_DELETED = IsDel.NOT_DELETED.value();

    private Integer id;

    private Integer wxAppId;

    private Integer wxMaUserId;

    private Integer addressId;

    private String outTradeNo;

    private String transactionId;

    private Integer totalFee;

    private Integer status;

    private String tradeStatus;

    private Integer expressTypeId;

    private String expressNo;

    private Integer isDel;

    private Date createTime;

    private Date updateTime;

    private Date successTime;

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

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Integer getExpressTypeId() {
        return expressTypeId;
    }

    public void setExpressTypeId(Integer expressTypeId) {
        this.expressTypeId = expressTypeId;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public void andLogicalDeleted(boolean deleted) {
        setIsDel(deleted ? IsDel.IS_DELETED.value() : IsDel.NOT_DELETED.value());
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", wxAppId=").append(wxAppId);
        sb.append(", wxMaUserId=").append(wxMaUserId);
        sb.append(", addressId=").append(addressId);
        sb.append(", outTradeNo=").append(outTradeNo);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", status=").append(status);
        sb.append(", tradeStatus=").append(tradeStatus);
        sb.append(", expressTypeId=").append(expressTypeId);
        sb.append(", expressNo=").append(expressNo);
        sb.append(", isDel=").append(isDel);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", successTime=").append(successTime);
        sb.append("]");
        return sb.toString();
    }

    public enum IsDel {
        NOT_DELETED(new Integer("0"), "未删除"),
        IS_DELETED(new Integer("1"), "已删除");

        private final Integer value;

        private final String name;

        IsDel(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return this.value;
        }

        public Integer value() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

        public static IsDel parseValue(Integer value) {
            if (value != null) {
                for (IsDel item : values()) {
                    if (item.value.equals(value)) {
                        return item;
                    }
                }
            }
            return null;
        }

        public static IsDel parseName(String name) {
            if (name != null) {
                for (IsDel item : values()) {
                    if (item.name.equals(name)) {
                        return item;
                    }
                }
            }
            return null;
        }
    }
}