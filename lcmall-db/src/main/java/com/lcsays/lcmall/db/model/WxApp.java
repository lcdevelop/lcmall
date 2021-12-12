package com.lcsays.lcmall.db.model;

import java.io.Serializable;

public class WxApp implements Serializable {
    private Integer id;

    private String name;

    private String appId;

    private String originalId;

    private String image;

    private Integer auth;

    private String wxaCodeUrl;

    private String type;

    private String mchId;

    private Boolean isDel;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getWxaCodeUrl() {
        return wxaCodeUrl;
    }

    public void setWxaCodeUrl(String wxaCodeUrl) {
        this.wxaCodeUrl = wxaCodeUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", appId=").append(appId);
        sb.append(", originalId=").append(originalId);
        sb.append(", image=").append(image);
        sb.append(", auth=").append(auth);
        sb.append(", wxaCodeUrl=").append(wxaCodeUrl);
        sb.append(", type=").append(type);
        sb.append(", mchId=").append(mchId);
        sb.append(", isDel=").append(isDel);
        sb.append("]");
        return sb.toString();
    }
}