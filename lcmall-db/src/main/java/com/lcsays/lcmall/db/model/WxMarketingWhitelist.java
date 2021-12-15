package com.lcsays.lcmall.db.model;

import java.io.Serializable;

public class WxMarketingWhitelist implements Serializable {
    private Integer id;

    private Integer batchNo;

    private String phoneNumber;

    private String userPhone;

    private String userPhoneEncrypt;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Integer batchNo) {
        this.batchNo = batchNo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhoneEncrypt() {
        return userPhoneEncrypt;
    }

    public void setUserPhoneEncrypt(String userPhoneEncrypt) {
        this.userPhoneEncrypt = userPhoneEncrypt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", userPhone=").append(userPhone);
        sb.append(", userPhoneEncrypt=").append(userPhoneEncrypt);
        sb.append("]");
        return sb.toString();
    }
}