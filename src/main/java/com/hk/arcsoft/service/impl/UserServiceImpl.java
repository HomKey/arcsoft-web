package com.hk.arcsoft.service.impl;

import com.hk.arcsoft.bo.UserInfoBO;
import com.hk.arcsoft.entity.UserInfo;
import com.hk.arcsoft.repository.UserRepository;
import com.hk.arcsoft.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HomKey on 2020/1/9.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserInfoBO> findAllByPage(Integer page, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<UserInfo> userInfos = userRepository.findAll(pageRequest);
        List<UserInfoBO> resultList = new ArrayList<>();
        for (UserInfo userInfo : userInfos.getContent()) {
            UserInfoBO userInfoBO = UserInfoBO.convertFor(userInfo);
            resultList.add(userInfoBO);
        }
        Page<UserInfoBO> userInfoBos = new PageImpl<>(resultList, userInfos.getPageable(), userInfos.getTotalElements());
        return userInfoBos;
    }

    @Override
    public UserInfoBO save(UserInfoBO userInfoBO) {
        UserInfo userInfo = userInfoBO.convertToUserInfo();
        userRepository.save(userInfo);
        userInfoBO = UserInfoBO.convertFor(userInfo);
        return userInfoBO;
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
