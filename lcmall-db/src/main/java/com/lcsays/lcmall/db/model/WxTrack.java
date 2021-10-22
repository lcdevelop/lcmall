package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxTrack implements Serializable {
    private Integer id;

    private Integer wxMaUserId;

    private Integer wxAppId;

    private String eventType;

    private String msg;

    private String ua;

    private String ip;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWxMaUserId() {
        return wxMaUserId;
    }

    public void setWxMaUserId(Integer wxMaUserId) {
        this.wxMaUserId = wxMaUserId;
    }

    public Integer getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(Integer wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxMaUserId=").append(wxMaUserId);
        sb.append(", wxAppId=").append(wxAppId);
        sb.append(", eventType=").append(eventType);
        sb.append(", msg=").append(msg);
        sb.append(", ua=").append(ua);
        sb.append(", ip=").append(ip);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}