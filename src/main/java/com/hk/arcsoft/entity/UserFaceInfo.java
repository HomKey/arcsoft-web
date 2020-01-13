package com.hk.arcsoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by HomKey on 2020/1/8.
 */
@Entity
@Table(name = "face_info")
public class UserFaceInfo extends BaseEntity {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "face_feature")
    private byte[] faceFeature;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public byte[] getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(byte[] faceFeature) {
        this.faceFeature = faceFeature;
    }
}
