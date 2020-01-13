package com.hk.arcsoft.repository;

import com.hk.arcsoft.entity.UserFaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by HomKey on 2020/1/9.
 */
@Repository
public interface FaceRepository extends JpaRepository<UserFaceInfo,Long>{

    List<UserFaceInfo> findAllByUserId(Long userId);
}
