package com.luwei.maven;

/**
 * Created by lhcpig on 2017/7/24.
 */
public class Entity {
    private int cmd;
    private String msg;
    private String comment;

    public Entity(int cmd, String msg, String comment) {
        this.cmd = cmd;
        this.msg = msg;
        this.comment = comment;
    }

    public int getCmd() {
        return cmd;
    }

    public String getMsg() {
        return msg;
    }

    public String getComment() {
        return comment;
    }
}
