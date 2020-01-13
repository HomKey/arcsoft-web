package com.hk.arcsoft.repository;

import com.hk.arcsoft.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by HomKey on 2020/1/9.
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
}
