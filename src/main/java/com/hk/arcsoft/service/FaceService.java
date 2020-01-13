package com.hk.arcsoft.service;

import com.hk.arcsoft.bo.FaceInfoBO;

/**
 * Created by HomKey on 2020/1/8.
 */
public interface FaceService {

    Long addUserFace(Long userId, String imageFile);

    void deleteUserFace(Long faceId);

    FaceInfoBO detectFace(String imageFile);

    boolean compareFace(Long userId, String targetImageFile, Float passRate);

}
