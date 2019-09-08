package com.hp.roam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysPlatformBoardExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SysPlatformBoardExample() {
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

        public Criteria andBoard_uuidIsNull() {
            addCriterion("board_uuid is null");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidIsNotNull() {
            addCriterion("board_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidEqualTo(String value) {
            addCriterion("board_uuid =", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidNotEqualTo(String value) {
            addCriterion("board_uuid <>", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidGreaterThan(String value) {
            addCriterion("board_uuid >", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidGreaterThanOrEqualTo(String value) {
            addCriterion("board_uuid >=", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidLessThan(String value) {
            addCriterion("board_uuid <", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidLessThanOrEqualTo(String value) {
            addCriterion("board_uuid <=", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidLike(String value) {
            addCriterion("board_uuid like", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidNotLike(String value) {
            addCriterion("board_uuid not like", value, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidIn(List<String> values) {
            addCriterion("board_uuid in", values, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidNotIn(List<String> values) {
            addCriterion("board_uuid not in", values, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidBetween(String value1, String value2) {
            addCriterion("board_uuid between", value1, value2, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andBoard_uuidNotBetween(String value1, String value2) {
            addCriterion("board_uuid not between", value1, value2, "board_uuid");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeIsNull() {
            addCriterion("platform_code is null");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeIsNotNull() {
            addCriterion("platform_code is not null");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeEqualTo(String value) {
            addCriterion("platform_code =", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeNotEqualTo(String value) {
            addCriterion("platform_code <>", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeGreaterThan(String value) {
            addCriterion("platform_code >", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeGreaterThanOrEqualTo(String value) {
            addCriterion("platform_code >=", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeLessThan(String value) {
            addCriterion("platform_code <", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeLessThanOrEqualTo(String value) {
            addCriterion("platform_code <=", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeLike(String value) {
            addCriterion("platform_code like", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeNotLike(String value) {
            addCriterion("platform_code not like", value, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeIn(List<String> values) {
            addCriterion("platform_code in", values, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeNotIn(List<String> values) {
            addCriterion("platform_code not in", values, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeBetween(String value1, String value2) {
            addCriterion("platform_code between", value1, value2, "platform_code");
            return (Criteria) this;
        }

        public Criteria andPlatform_codeNotBetween(String value1, String value2) {
            addCriterion("platform_code not between", value1, value2, "platform_code");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameIsNull() {
            addCriterion("openfire_username is null");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameIsNotNull() {
            addCriterion("openfire_username is not null");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameEqualTo(String value) {
            addCriterion("openfire_username =", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameNotEqualTo(String value) {
            addCriterion("openfire_username <>", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameGreaterThan(String value) {
            addCriterion("openfire_username >", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameGreaterThanOrEqualTo(String value) {
            addCriterion("openfire_username >=", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameLessThan(String value) {
            addCriterion("openfire_username <", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameLessThanOrEqualTo(String value) {
            addCriterion("openfire_username <=", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameLike(String value) {
            addCriterion("openfire_username like", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameNotLike(String value) {
            addCriterion("openfire_username not like", value, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameIn(List<String> values) {
            addCriterion("openfire_username in", values, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameNotIn(List<String> values) {
            addCriterion("openfire_username not in", values, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameBetween(String value1, String value2) {
            addCriterion("openfire_username between", value1, value2, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_usernameNotBetween(String value1, String value2) {
            addCriterion("openfire_username not between", value1, value2, "openfire_username");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordIsNull() {
            addCriterion("openfire_password is null");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordIsNotNull() {
            addCriterion("openfire_password is not null");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordEqualTo(String value) {
            addCriterion("openfire_password =", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordNotEqualTo(String value) {
            addCriterion("openfire_password <>", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordGreaterThan(String value) {
            addCriterion("openfire_password >", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordGreaterThanOrEqualTo(String value) {
            addCriterion("openfire_password >=", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordLessThan(String value) {
            addCriterion("openfire_password <", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordLessThanOrEqualTo(String value) {
            addCriterion("openfire_password <=", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordLike(String value) {
            addCriterion("openfire_password like", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordNotLike(String value) {
            addCriterion("openfire_password not like", value, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordIn(List<String> values) {
            addCriterion("openfire_password in", values, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordNotIn(List<String> values) {
            addCriterion("openfire_password not in", values, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordBetween(String value1, String value2) {
            addCriterion("openfire_password between", value1, value2, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andOpenfire_passwordNotBetween(String value1, String value2) {
            addCriterion("openfire_password not between", value1, value2, "openfire_password");
            return (Criteria) this;
        }

        public Criteria andCreate_timeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreate_timeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreate_timeEqualTo(Date value) {
            addCriterion("create_time =", value, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeGreaterThan(Date value) {
            addCriterion("create_time >", value, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeLessThan(Date value) {
            addCriterion("create_time <", value, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeIn(List<Date> values) {
            addCriterion("create_time in", values, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "create_time");
            return (Criteria) this;
        }

        public Criteria andCreate_timeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "create_time");
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