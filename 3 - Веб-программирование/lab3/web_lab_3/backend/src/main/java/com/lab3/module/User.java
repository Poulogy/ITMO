package com.lab3.module;

public class User {
    private String username;
    private String passwordHash;
    private String refreshToken;

    public User() {
    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}