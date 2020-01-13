package com.hk.arcsoft.dto;

import com.google.common.base.Converter;
import com.hk.arcsoft.bo.UserInfoBO;
import org.springframework.beans.BeanUtils;

/**
 * Created by HomKey on 2020/1/9.
 */
public class UserInfoWithPwdDTO {
    private Long id;
    private String name;
    private String account;
    private String password;
    private String passwordRepeat;

    public UserInfoBO convertToUserInfoBO() {
        UserInfoDTOConvert userInfoBoConvert = new UserInfoDTOConvert();
        UserInfoBO convert = userInfoBoConvert.convert(this);
        return convert;
    }

    public static UserInfoWithPwdDTO convertFor(UserInfoBO userInfo) {
        UserInfoDTOConvert userInfoBoConvert = new UserInfoDTOConvert();
        UserInfoWithPwdDTO convert = userInfoBoConvert.reverse().convert(userInfo);
        return convert;
    }

    private static class UserInfoDTOConvert extends Converter<UserInfoWithPwdDTO, UserInfoBO> {

        @Override
        protected UserInfoBO doForward(UserInfoWithPwdDTO userInfoDto) {
            if (!userInfoDto.getPassword().equals(userInfoDto.getPasswordRepeat())) {
                return null;
            }
            UserInfoBO userInfo = new UserInfoBO();
            BeanUtils.copyProperties(userInfoDto, userInfo);
            return userInfo;
        }

        @Override
        protected UserInfoWithPwdDTO doBackward(UserInfoBO userInfo) {
            UserInfoWithPwdDTO userInfoBo = new UserInfoWithPwdDTO();
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

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }
}
