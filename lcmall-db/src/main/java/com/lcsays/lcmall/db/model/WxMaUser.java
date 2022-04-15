package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxMaUser implements Serializable {
    private Integer id;

    private Integer wxAppId;

    private String phoneNumber;

    private String myTempNote;

    private String userPhone;

    private String userPhoneEncrypt;

    private Date createTime;

    private Date updateTime;

    private String role;

    private String nickname;

    private Integer sessionWxAppId;

    private String sessionKey;

    private String openid;

    private String unionid;

    private String avatarUrl;

    private String gender;

    private String language;

    private String country;

    private String city;

    private String token;

    private String email;

    private String code;

    private Date codeExpire;

    private String password;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMyTempNote() {
        return myTempNote;
    }

    public void setMyTempNote(String myTempNote) {
        this.myTempNote = myTempNote;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSessionWxAppId() {
        return sessionWxAppId;
    }

    public void setSessionWxAppId(Integer sessionWxAppId) {
        this.sessionWxAppId = sessionWxAppId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCodeExpire() {
        return codeExpire;
    }

    public void setCodeExpire(Date codeExpire) {
        this.codeExpire = codeExpire;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", wxAppId=").append(wxAppId);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", myTempNote=").append(myTempNote);
        sb.append(", userPhone=").append(userPhone);
        sb.append(", userPhoneEncrypt=").append(userPhoneEncrypt);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", role=").append(role);
        sb.append(", nickname=").append(nickname);
        sb.append(", sessionWxAppId=").append(sessionWxAppId);
        sb.append(", sessionKey=").append(sessionKey);
        sb.append(", openid=").append(openid);
        sb.append(", unionid=").append(unionid);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", gender=").append(gender);
        sb.append(", language=").append(language);
        sb.append(", country=").append(country);
        sb.append(", city=").append(city);
        sb.append(", token=").append(token);
        sb.append(", email=").append(email);
        sb.append(", code=").append(code);
        sb.append(", codeExpire=").append(codeExpire);
        sb.append(", password=").append(password);
        sb.append("]");
        return sb.toString();
    }
}