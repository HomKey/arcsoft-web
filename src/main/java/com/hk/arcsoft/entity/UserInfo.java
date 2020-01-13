package com.hk.arcsoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by HomKey on 2020/1/8.
 */
@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "account")
    private String account;
    @Column(name = "password")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
