package com.anhtuan.pojo;

public class TodoEntry {
    private String value;
    private Long date;
    private String language;
    private String keywordCategory;

    public TodoEntry(String value, Long date, String language, String keywordCategory) {
        this.value = value;
        this.date = date;
        this.language = language;
        this.keywordCategory = keywordCategory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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