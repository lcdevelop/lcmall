package com.lcsays.lcmall.db.model;

public class WxApp {
    private Integer id;

    private String name;

    private String appId;

    private String image;

    private Integer auth;

    private String wxaCodeUrl;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", appId=").append(appId);
        sb.append(", image=").append(image);
        sb.append(", auth=").append(auth);
        sb.append(", wxaCodeUrl=").append(wxaCodeUrl);
        sb.append("]");
        return sb.toString();
    }
}