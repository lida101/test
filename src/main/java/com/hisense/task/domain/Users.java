package com.hisense.task.domain;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class Users {
	
	private static final long serialVersionUID = 2760421212021508566L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name= "user_code")
    private String userCode;

    @Column(name= "user_name")
    private String userName;

    @Column(name= "user_name_cn")
    private String userNameCn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameCn() {
        return userNameCn;
    }

    public void setUserNameCn(String userNameCn) {
        this.userNameCn = userNameCn;
    }
}
