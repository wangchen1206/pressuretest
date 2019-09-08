package com.hp.roam.model;

import java.util.Date;

public class SysPlatformBoard {
    private String board_uuid;

    private String platform_code;

    private String openfire_username;

    private String openfire_password;

    private Date create_time;

    public String getBoard_uuid() {
        return board_uuid;
    }

    public void setBoard_uuid(String board_uuid) {
        this.board_uuid = board_uuid == null ? null : board_uuid.trim();
    }

    public String getPlatform_code() {
        return platform_code;
    }

    public void setPlatform_code(String platform_code) {
        this.platform_code = platform_code == null ? null : platform_code.trim();
    }

    public String getOpenfire_username() {
        return openfire_username;
    }

    public void setOpenfire_username(String openfire_username) {
        this.openfire_username = openfire_username == null ? null : openfire_username.trim();
    }

    public String getOpenfire_password() {
        return openfire_password;
    }

    public void setOpenfire_password(String openfire_password) {
        this.openfire_password = openfire_password == null ? null : openfire_password.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}