package com.hp.roam.model;

import java.time.LocalDateTime;

public class ReceiveMsg {
    private String random_id;

    private String username;

    private String command;

    private LocalDateTime receive_time;

    private String msg_content;

    public String getRandom_id() {
        return random_id;
    }

    public void setRandom_id(String random_id) {
        this.random_id = random_id == null ? null : random_id.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command == null ? null : command.trim();
    }

    public LocalDateTime getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(LocalDateTime receive_time) {
        this.receive_time = receive_time;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content == null ? null : msg_content.trim();
    }

	@Override
	public String toString() {
		return "ReceiveMsg [random_id=" + random_id + ", username=" + username
				+ ", command=" + command + ", receive_time=" + receive_time
				+ "]";
	}
    
    
}