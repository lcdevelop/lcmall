package com.lcsays.lcmall.db.model;

import java.io.Serializable;
import java.util.Date;

public class EcProduct implements Serializable {
    public static final Byte IS_DELETED = IsDel.IS_DELETED.value();

    public static final Byte NOT_DELETED = IsDel.NOT_DELETED.value();

    private Integer id;

    private String name;

    private String description;

    private Integer categoryId;

    private Byte isDel;

    private Date createTime;

    private Date updateTime;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        sb.append(", description=").append(description);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", isDel=").append(isDel);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    public enum IsDel {
        NOT_DELETED(new Byte("0"), "?????????"),
        IS_DELETED(new Byte("1"), "?????????");

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