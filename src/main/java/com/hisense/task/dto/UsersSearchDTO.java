package com.hisense.task.dto;

import javax.persistence.Column;
import java.io.Serializable;

public class UsersSearchDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private String userCode;

    private String userName;

    private String userNameCn;

    public String getUserCode() {
        return userCode==null?"":userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName==null?"":userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameCn() {
        return userNameCn==null?"":userNameCn;
    }

    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }
}
