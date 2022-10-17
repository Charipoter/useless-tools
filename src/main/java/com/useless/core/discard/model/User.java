package com.useless.core.discard.model;

import lombok.Data;
import java.sql.Date;

@Data
public class User {

    private String account;

    private String password;
    // true - male, false - female
    private boolean gender;

    private Date birth;

    private String interest;
    // 备注
    private String remark;

    public User() {}

    public User(String account, String password, boolean gender, Date birth, String interest, String remark) {
        this.account = account;
        this.password = password;
        this.gender = gender;
        this.birth = birth;
        this.interest = interest;
        this.remark = remark;
    }

}
