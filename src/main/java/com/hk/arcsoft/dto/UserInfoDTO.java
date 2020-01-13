package com.hk.arcsoft.dto;

import com.google.common.base.Converter;
import com.hk.arcsoft.bo.UserInfoBO;
import org.springframework.beans.BeanUtils;

/**
 * Created by HomKey on 2020/1/9.
 */
public class UserInfoDTO {
    private Long id;
    private String name;
    private String account;

    public UserInfoBO convertToUserInfoBO() {
        UserInfoDTOConvert userInfoBoConvert = new UserInfoDTOConvert();
        UserInfoBO convert = userInfoBoConvert.convert(this);
        return convert;
    }

    public static UserInfoDTO convertFor(UserInfoBO userInfo) {
        UserInfoDTOConvert userInfoBoConvert = new UserInfoDTOConvert();
        UserInfoDTO convert = userInfoBoConvert.reverse().convert(userInfo);
        return convert;
    }

    private static class UserInfoDTOConvert extends Converter<UserInfoDTO, UserInfoBO> {

        @Override
        protected UserInfoBO doForward(UserInfoDTO userInfoDto) {
            UserInfoBO userInfo = new UserInfoBO();
            BeanUtils.copyProperties(userInfoDto, userInfo);
            return userInfo;
        }

        @Override
        protected UserInfoDTO doBackward(UserInfoBO userInfo) {
            UserInfoDTO userInfoBo = new UserInfoDTO();
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
}
