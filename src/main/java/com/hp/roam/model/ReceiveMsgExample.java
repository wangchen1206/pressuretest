package com.hp.roam.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceiveMsgExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReceiveMsgExample() {
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

        public Criteria andRandom_idIsNull() {
            addCriterion("random_id is null");
            return (Criteria) this;
        }

        public Criteria andRandom_idIsNotNull() {
            addCriterion("random_id is not null");
            return (Criteria) this;
        }

        public Criteria andRandom_idEqualTo(String value) {
            addCriterion("random_id =", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idNotEqualTo(String value) {
            addCriterion("random_id <>", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idGreaterThan(String value) {
            addCriterion("random_id >", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idGreaterThanOrEqualTo(String value) {
            addCriterion("random_id >=", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idLessThan(String value) {
            addCriterion("random_id <", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idLessThanOrEqualTo(String value) {
            addCriterion("random_id <=", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idLike(String value) {
            addCriterion("random_id like", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idNotLike(String value) {
            addCriterion("random_id not like", value, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idIn(List<String> values) {
            addCriterion("random_id in", values, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idNotIn(List<String> values) {
            addCriterion("random_id not in", values, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idBetween(String value1, String value2) {
            addCriterion("random_id between", value1, value2, "random_id");
            return (Criteria) this;
        }

        public Criteria andRandom_idNotBetween(String value1, String value2) {
            addCriterion("random_id not between", value1, value2, "random_id");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andCommandIsNull() {
            addCriterion("command is null");
            return (Criteria) this;
        }

        public Criteria andCommandIsNotNull() {
            addCriterion("command is not null");
            return (Criteria) this;
        }

        public Criteria andCommandEqualTo(String value) {
            addCriterion("command =", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandNotEqualTo(String value) {
            addCriterion("command <>", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandGreaterThan(String value) {
            addCriterion("command >", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandGreaterThanOrEqualTo(String value) {
            addCriterion("command >=", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandLessThan(String value) {
            addCriterion("command <", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandLessThanOrEqualTo(String value) {
            addCriterion("command <=", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandLike(String value) {
            addCriterion("command like", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandNotLike(String value) {
            addCriterion("command not like", value, "command");
            return (Criteria) this;
        }

        public Criteria andCommandIn(List<String> values) {
            addCriterion("command in", values, "command");
            return (Criteria) this;
        }

        public Criteria andCommandNotIn(List<String> values) {
            addCriterion("command not in", values, "command");
            return (Criteria) this;
        }

        public Criteria andCommandBetween(String value1, String value2) {
            addCriterion("command between", value1, value2, "command");
            return (Criteria) this;
        }

        public Criteria andCommandNotBetween(String value1, String value2) {
            addCriterion("command not between", value1, value2, "command");
            return (Criteria) this;
        }

        public Criteria andReceive_timeIsNull() {
            addCriterion("receive_time is null");
            return (Criteria) this;
        }

        public Criteria andReceive_timeIsNotNull() {
            addCriterion("receive_time is not null");
            return (Criteria) this;
        }

        public Criteria andReceive_timeEqualTo(LocalDateTime value) {
            addCriterion("receive_time =", value, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeNotEqualTo(LocalDateTime value) {
            addCriterion("receive_time <>", value, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeGreaterThan(LocalDateTime value) {
            addCriterion("receive_time >", value, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("receive_time >=", value, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeLessThan(LocalDateTime value) {
            addCriterion("receive_time <", value, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("receive_time <=", value, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeIn(List<LocalDateTime> values) {
            addCriterion("receive_time in", values, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeNotIn(List<LocalDateTime> values) {
            addCriterion("receive_time not in", values, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("receive_time between", value1, value2, "receive_time");
            return (Criteria) this;
        }

        public Criteria andReceive_timeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("receive_time not between", value1, value2, "receive_time");
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