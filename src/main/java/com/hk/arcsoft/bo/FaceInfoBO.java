package com.hk.arcsoft.bo;

import com.google.common.base.Converter;
import com.hk.arcsoft.entity.UserFaceInfo;
import org.springframework.beans.BeanUtils;

/**
 * Created by HomKey on 2020/1/9.
 */
public class FaceInfoBO {
    private Long userId;
    private String imageFile;

    private Integer age;
    private Integer gender;
    private Integer liveness;

    public UserFaceInfo convertToUserFaceInfo() {
        FaceInfoBOConvert faceInfoBoConvert = new FaceInfoBOConvert();
        UserFaceInfo convert = faceInfoBoConvert.convert(this);
        return convert;
    }

    public static FaceInfoBO convertFor(UserFaceInfo userFaceInfo) {
        FaceInfoBOConvert faceInfoBoConvert = new FaceInfoBOConvert();
        FaceInfoBO convert = faceInfoBoConvert.reverse().convert(userFaceInfo);
        return convert;
    }

    private static class FaceInfoBOConvert extends Converter<FaceInfoBO, UserFaceInfo> {

        @Override
        protected UserFaceInfo doForward(FaceInfoBO faceInfoBo) {
            UserFaceInfo userFaceInfo = new UserFaceInfo();
            BeanUtils.copyProperties(faceInfoBo, userFaceInfo);
            return userFaceInfo;
        }

        @Override
        protected FaceInfoBO doBackward(UserFaceInfo userFaceInfo) {
            FaceInfoBO faceInfoBo = new FaceInfoBO();
            BeanUtils.copyProperties(userFaceInfo, faceInfoBo);
            return faceInfoBo;
        }
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getLiveness() {
        return liveness;
    }

    public void setLiveness(Integer liveness) {
        this.liveness = liveness;
    }
}
