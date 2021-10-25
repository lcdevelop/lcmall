package com.lcsays.lcmall.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WxMarketingActivityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WxMarketingActivityExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andWxAppIdIsNull() {
            addCriterion("wx_app_id is null");
            return (Criteria) this;
        }

        public Criteria andWxAppIdIsNotNull() {
            addCriterion("wx_app_id is not null");
            return (Criteria) this;
        }

        public Criteria andWxAppIdEqualTo(Integer value) {
            addCriterion("wx_app_id =", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotEqualTo(Integer value) {
            addCriterion("wx_app_id <>", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdGreaterThan(Integer value) {
            addCriterion("wx_app_id >", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("wx_app_id >=", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdLessThan(Integer value) {
            addCriterion("wx_app_id <", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdLessThanOrEqualTo(Integer value) {
            addCriterion("wx_app_id <=", value, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdIn(List<Integer> values) {
            addCriterion("wx_app_id in", values, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotIn(List<Integer> values) {
            addCriterion("wx_app_id not in", values, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdBetween(Integer value1, Integer value2) {
            addCriterion("wx_app_id between", value1, value2, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andWxAppIdNotBetween(Integer value1, Integer value2) {
            addCriterion("wx_app_id not between", value1, value2, "wxAppId");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeIsNull() {
            addCriterion("template_type is null");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeIsNotNull() {
            addCriterion("template_type is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeEqualTo(Integer value) {
            addCriterion("template_type =", value, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeNotEqualTo(Integer value) {
            addCriterion("template_type <>", value, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeGreaterThan(Integer value) {
            addCriterion("template_type >", value, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("template_type >=", value, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeLessThan(Integer value) {
            addCriterion("template_type <", value, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeLessThanOrEqualTo(Integer value) {
            addCriterion("template_type <=", value, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeIn(List<Integer> values) {
            addCriterion("template_type in", values, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeNotIn(List<Integer> values) {
            addCriterion("template_type not in", values, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeBetween(Integer value1, Integer value2) {
            addCriterion("template_type between", value1, value2, "templateType");
            return (Criteria) this;
        }

        public Criteria andTemplateTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("template_type not between", value1, value2, "templateType");
            return (Criteria) this;
        }

        public Criteria andStockIdListIsNull() {
            addCriterion("stock_id_list is null");
            return (Criteria) this;
        }

        public Criteria andStockIdListIsNotNull() {
            addCriterion("stock_id_list is not null");
            return (Criteria) this;
        }

        public Criteria andStockIdListEqualTo(String value) {
            addCriterion("stock_id_list =", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListNotEqualTo(String value) {
            addCriterion("stock_id_list <>", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListGreaterThan(String value) {
            addCriterion("stock_id_list >", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListGreaterThanOrEqualTo(String value) {
            addCriterion("stock_id_list >=", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListLessThan(String value) {
            addCriterion("stock_id_list <", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListLessThanOrEqualTo(String value) {
            addCriterion("stock_id_list <=", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListLike(String value) {
            addCriterion("stock_id_list like", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListNotLike(String value) {
            addCriterion("stock_id_list not like", value, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListIn(List<String> values) {
            addCriterion("stock_id_list in", values, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListNotIn(List<String> values) {
            addCriterion("stock_id_list not in", values, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListBetween(String value1, String value2) {
            addCriterion("stock_id_list between", value1, value2, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andStockIdListNotBetween(String value1, String value2) {
            addCriterion("stock_id_list not between", value1, value2, "stockIdList");
            return (Criteria) this;
        }

        public Criteria andUrlLinkIsNull() {
            addCriterion("url_link is null");
            return (Criteria) this;
        }

        public Criteria andUrlLinkIsNotNull() {
            addCriterion("url_link is not null");
            return (Criteria) this;
        }

        public Criteria andUrlLinkEqualTo(String value) {
            addCriterion("url_link =", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkNotEqualTo(String value) {
            addCriterion("url_link <>", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkGreaterThan(String value) {
            addCriterion("url_link >", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkGreaterThanOrEqualTo(String value) {
            addCriterion("url_link >=", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkLessThan(String value) {
            addCriterion("url_link <", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkLessThanOrEqualTo(String value) {
            addCriterion("url_link <=", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkLike(String value) {
            addCriterion("url_link like", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkNotLike(String value) {
            addCriterion("url_link not like", value, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkIn(List<String> values) {
            addCriterion("url_link in", values, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkNotIn(List<String> values) {
            addCriterion("url_link not in", values, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkBetween(String value1, String value2) {
            addCriterion("url_link between", value1, value2, "urlLink");
            return (Criteria) this;
        }

        public Criteria andUrlLinkNotBetween(String value1, String value2) {
            addCriterion("url_link not between", value1, value2, "urlLink");
            return (Criteria) this;
        }

        public Criteria andExtraVersionIsNull() {
            addCriterion("extra_version is null");
            return (Criteria) this;
        }

        public Criteria andExtraVersionIsNotNull() {
            addCriterion("extra_version is not null");
            return (Criteria) this;
        }

        public Criteria andExtraVersionEqualTo(Integer value) {
            addCriterion("extra_version =", value, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionNotEqualTo(Integer value) {
            addCriterion("extra_version <>", value, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionGreaterThan(Integer value) {
            addCriterion("extra_version >", value, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("extra_version >=", value, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionLessThan(Integer value) {
            addCriterion("extra_version <", value, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionLessThanOrEqualTo(Integer value) {
            addCriterion("extra_version <=", value, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionIn(List<Integer> values) {
            addCriterion("extra_version in", values, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionNotIn(List<Integer> values) {
            addCriterion("extra_version not in", values, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionBetween(Integer value1, Integer value2) {
            addCriterion("extra_version between", value1, value2, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andExtraVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("extra_version not between", value1, value2, "extraVersion");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}