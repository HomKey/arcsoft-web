package com.hk.arcsoft.dto;

import com.google.common.base.Converter;
import com.hk.arcsoft.bo.FaceInfoBO;
import org.springframework.beans.BeanUtils;

/**
 * Created by HomKey on 2020/1/9.
 */
public class FaceInfoDTO {
    private Long id;
    private Long userId;
    private Integer age;
    private Integer gender;
    private Integer liveness;

//    public FaceInfoBO convertToFaceInfo() {
//        FaceInfoDTOConvert faceInfoDTOConvert = new FaceInfoDTOConvert();
//        FaceInfoBO convert = faceInfoDTOConvert.convert(this);
//        return convert;
//    }

    public static FaceInfoDTO convertFor(FaceInfoBO faceInfoBO) {
        FaceInfoDTOConvert faceInfoDTOConvert = new FaceInfoDTOConvert();
        FaceInfoDTO convert = faceInfoDTOConvert.reverse().convert(faceInfoBO);
        return convert;
    }

    private static class FaceInfoDTOConvert extends Converter<FaceInfoDTO, FaceInfoBO> {

        @Override
        protected FaceInfoBO doForward(FaceInfoDTO faceInfoDto) {
//            FaceInfoBO faceInfoBO = new FaceInfoBO();
//            BeanUtils.copyProperties(faceInfoDto, faceInfoBO);
//            return faceInfoBO;
            return null;
        }

        @Override
        protected FaceInfoDTO doBackward(FaceInfoBO faceInfoBO) {
            FaceInfoDTO faceInfoDTO = new FaceInfoDTO();
            BeanUtils.copyProperties(faceInfoBO, faceInfoDTO);
            return faceInfoDTO;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
