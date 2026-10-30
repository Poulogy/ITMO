package com.lab3.module;

public class PointBean {
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private String serverTime;
    private long scriptTime;
    private String username;

    public PointBean() {}


    public PointBean(double x, double y, double r, boolean hit, String serverTime, long scriptTime, String username) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.serverTime = serverTime;
        this.scriptTime = scriptTime;
        this.username = username;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
    public boolean isHit() { return hit; }
    public String getServerTime() { return serverTime; }
    public long getScriptTime() { return scriptTime; }
    public String getUsername() { return username; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setR(double r) { this.r = r; }
    public void setHit(boolean hit) { this.hit = hit; }
    public void setServerTime(String serverTime) { this.serverTime = serverTime; }
    public void setScriptTime(long scriptTime) { this.scriptTime = scriptTime; }
    public void setUsername(String username) { this.username = username; }

}