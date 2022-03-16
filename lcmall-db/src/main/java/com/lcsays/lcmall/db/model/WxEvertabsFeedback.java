package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxEvertabsFeedback implements Serializable {
    private Integer id;

    private Integer wxMaUserId;

    private String email;

    private String content;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        sb.append(", email=").append(email);
        sb.append(", content=").append(content);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}