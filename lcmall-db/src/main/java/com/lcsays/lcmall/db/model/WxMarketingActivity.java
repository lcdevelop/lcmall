package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxMarketingActivity implements Serializable {
    private Integer id;

    private String name;

    private Integer wxAppId;

    private Integer templateType;

    private String stockIdList;

    private String urlLink;

    private Integer extraVersion;

    private Integer whitelistBatchNo;

    private Date createTime;

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

    public Integer getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(Integer wxAppId) {
        this.wxAppId = wxAppId;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getStockIdList() {
        return stockIdList;
    }

    public void setStockIdList(String stockIdList) {
        this.stockIdList = stockIdList;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public Integer getExtraVersion() {
        return extraVersion;
    }

    public void setExtraVersion(Integer extraVersion) {
        this.extraVersion = extraVersion;
    }

    public Integer getWhitelistBatchNo() {
        return whitelistBatchNo;
    }

    public void setWhitelistBatchNo(Integer whitelistBatchNo) {
        this.whitelistBatchNo = whitelistBatchNo;
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
        sb.append(", name=").append(name);
        sb.append(", wxAppId=").append(wxAppId);
        sb.append(", templateType=").append(templateType);
        sb.append(", stockIdList=").append(stockIdList);
        sb.append(", urlLink=").append(urlLink);
        sb.append(", extraVersion=").append(extraVersion);
        sb.append(", whitelistBatchNo=").append(whitelistBatchNo);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}