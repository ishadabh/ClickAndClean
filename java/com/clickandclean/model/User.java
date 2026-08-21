package com.clickandclean.model;

public class User {

    private int userId;
    private String name;
    private String loginId;
    private String password;
    private String role;
    private int points;

    public User() {
    }

    public User(int userId, String name, String loginId,
                String password, String role, int points) {

        this.userId = userId;
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
        this.points = points;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}