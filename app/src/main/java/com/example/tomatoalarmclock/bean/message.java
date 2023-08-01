package com.example.tomatoalarmclock.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


public class message extends LitePalSupport{

    @Column(unique = true, defaultValue = "unknown")
    private String event;

    public message(String event, float time, int state) {
        this.event = event;
        this.time = time;
        this.state = state;
    }

    public message() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private float time;

    private  int state;

}

