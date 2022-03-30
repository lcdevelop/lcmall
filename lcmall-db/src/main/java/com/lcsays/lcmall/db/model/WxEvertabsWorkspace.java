package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class WxEvertabsWorkspace implements Serializable {
    public static final Byte IS_DELETED = IsDel.IS_DELETED.value();

    public static final Byte NOT_DELETED = IsDel.NOT_DELETED.value();

    private Integer id;

    private String name;

    private Boolean isTemp;

    private String color;

    private Integer wxMaUserId;

    private Byte isDel;

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

    public Boolean getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(Boolean isTemp) {
        this.isTemp = isTemp;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getWxMaUserId() {
        return wxMaUserId;
    }

    public void setWxMaUserId(Integer wxMaUserId) {
        this.wxMaUserId = wxMaUserId;
    }

    public void andLogicalDeleted(boolean deleted) {
        setIsDel(deleted ? IsDel.IS_DELETED.value() : IsDel.NOT_DELETED.value());
    }

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
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
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", isTemp=").append(isTemp);
        sb.append(", color=").append(color);
        sb.append(", wxMaUserId=").append(wxMaUserId);
        sb.append(", isDel=").append(isDel);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }

    public enum IsDel {
        NOT_DELETED(new Byte("0"), "未删除"),
        IS_DELETED(new Byte("1"), "已删除");

        private final Byte value;

        private final String name;

        IsDel(Byte value, String name) {
            this.value = value;
            this.name = name;
        }

        public Byte getValue() {
            return this.value;
        }

        public Byte value() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

        public static IsDel parseValue(Byte value) {
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