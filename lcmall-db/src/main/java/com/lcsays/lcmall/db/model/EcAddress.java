package com.lcsays.lcmall.db.model;

import java.io.Serializable;

public class EcAddress implements Serializable {
    public static final Integer IS_DELETED = IsDel.IS_DELETED.value();

    public static final Integer NOT_DELETED = IsDel.NOT_DELETED.value();

    private Integer id;

    private Integer wxMaUserId;

    private String name;

    private String phone;

    private String address;

    private Integer isDel;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", wxMaUserId=").append(wxMaUserId);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", address=").append(address);
        sb.append(", isDel=").append(isDel);
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