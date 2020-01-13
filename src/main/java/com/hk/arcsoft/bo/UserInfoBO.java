package com.hk.arcsoft.bo;

import com.google.common.base.Converter;
import com.hk.arcsoft.entity.UserInfo;
import org.springframework.beans.BeanUtils;

/**
 * Created by HomKey on 2020/1/9.
 */
public class UserInfoBO {
    private Long id;
    private String name;
    private String account;
    private String password;

    public UserInfo convertToUserInfo() {
        UserInfoBOConvert userInfoBoConvert = new UserInfoBOConvert();
        UserInfo convert = userInfoBoConvert.convert(this);
        return convert;
    }

    public static UserInfoBO convertFor(UserInfo userInfo) {
        UserInfoBOConvert userInfoBoConvert = new UserInfoBOConvert();
        UserInfoBO convert = userInfoBoConvert.reverse().convert(userInfo);
        return convert;
    }

    private static class UserInfoBOConvert extends Converter<UserInfoBO, UserInfo> {

        @Override
        protected UserInfo doForward(UserInfoBO userInfoBo) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(userInfoBo, userInfo);
            return userInfo;
        }

        @Override
        protected UserInfoBO doBackward(UserInfo userInfo) {
            UserInfoBO userInfoBo = new UserInfoBO();
            BeanUtils.copyProperties(userInfo, userInfoBo);
            return userInfoBo;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
