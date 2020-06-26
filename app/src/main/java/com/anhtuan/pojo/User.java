package com.anhtuan.pojo;

import java.util.ArrayList;

public class User {
    private String userId;
    private String userShortName;
    private String userName;
    private ArrayList<String> todoListGroups;
    private ArrayList<String> expenseGroups;

    public User() {
        this.userId = "";
        this.userName = "";
        this.userShortName = "";
        this.todoListGroups = new ArrayList<>();
        this.expenseGroups = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getTodoListGroups() {
        return todoListGroups;
    }

    public void setTodoListGroups(ArrayList<String> todoListGroups) {
        this.todoListGroups = todoListGroups;
    }

    public ArrayList<String> getExpenseGroups() {
        return expenseGroups;
    }

    public void setExpenseGroups(ArrayList<String> expenseGroups) {
        this.expenseGroups = expenseGroups;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserShortName() {
        return userShortName;
    }

    public void setUserShortName(String userShortName) {
        this.userShortName = userShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
