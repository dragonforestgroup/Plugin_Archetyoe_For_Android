package com.dragonforest.plugin.archetype.model;

public class Result {
    boolean isOk = true;
    String msg = "validate pass";

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
