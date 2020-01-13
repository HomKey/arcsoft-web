package com.hk.arcsoft.service;

import com.hk.arcsoft.bo.UserInfoBO;
import org.springframework.data.domain.Page;

/**
 * Created by HomKey on 2020/1/9.
 */
public interface UserService {
    Page<UserInfoBO> findAllByPage(Integer page, Integer pageSize);

    UserInfoBO save(UserInfoBO userInfoBO);

    void delete(Long userId);
}
