package com.anhtuan.pojo;

public class TodoEntry {
    private String value;
    private Long date;
    private String language;
    private String keywordCategory;
    private Long amount;
    private String groupId;
    private String createBy;

    public TodoEntry(String value, Long date, String language, String keywordCategory, Long amount, String groupId, String createBy) {
        this.value = value;
        this.date = date;
        this.language = language;
        this.keywordCategory = keywordCategory;
        this.amount = amount;
        this.groupId = groupId;
        this.createBy = createBy;
    }

    public String getValue() {
        return value;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getKeywordCategory() {
        return keywordCategory;
    }

    public void setKeywordCategory(String keywordCategory) {
        this.keywordCategory = keywordCategory;
    }
}
