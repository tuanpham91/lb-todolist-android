package com.anhtuan.pojo;

public class UserGroup {
    private String userId;
    private String groupId;
    private String groupType;

    public static final String todoGroupType = "TodoGroup";
    public static final String expenseGroupType = "ExpenseGroup";

    public UserGroup(String userId, String groupId, String groupType) {
        this.userId = userId;
        this.groupId = groupId;
        this.groupType = groupType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
