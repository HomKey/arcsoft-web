package com.hk.arcsoft.service.impl;

import com.arcsoft.face.*;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.google.common.collect.Lists;
import com.hk.arcsoft.bo.FaceInfoBO;
import com.hk.arcsoft.core.factory.FaceEngineFactory;
import com.hk.arcsoft.entity.UserFaceInfo;
import com.hk.arcsoft.repository.FaceRepository;
import com.hk.arcsoft.repository.UserRepository;
import com.hk.arcsoft.service.FaceService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by HomKey on 2020/1/8.
 */
@Slf4j
@Service
public class ArcSoftFaceServiceImpl implements FaceService {

    @Value("${arcsoft.sdk.libPath}")
    public String sdkLibPath;
    @Value("${arcsoft.sdk.appId}")
    public String appId;
    @Value("${arcsoft.sdk.key}")
    public String sdkKey;
    @Value("${arcsoft.sdk.threadPoolSize}")
    public Integer threadPoolSize;

    private FunctionConfiguration functionConfiguration;
    private GenericObjectPool<FaceEngine> faceEngineObjectPool;

    public static final float PASS_RATE = 0.8f;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FaceRepository faceRepository;

    @PostConstruct
    public void init() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
//        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

        //功能配置
        functionConfiguration = FunctionConfiguration.builder()
                .supportAge(true)
                .supportFace3dAngle(true)
                .supportFaceDetect(true)
                .supportFaceRecognition(true)
                .supportGender(true)
                .supportLiveness(true)
                .supportIRLiveness(true)
                .build();
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        faceEngineObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, sdkLibPath, engineConfiguration), poolConfig);

    }

    @Override
    public Long addUserFace(Long userId, String imageFile) {
        ImageInfo imageInfo = getImageInfo(imageFile);
        byte[] bytes = extractFaceFeature(imageInfo);
        if (bytes == null) {
            return null;
        }
        UserFaceInfo userFaceInfo = new UserFaceInfo();
        userFaceInfo.setUserId(userId);
        userFaceInfo.setFaceFeature(bytes);
        UserFaceInfo result = faceRepository.save(userFaceInfo);
        return Optional.ofNullable(result).orElse(new UserFaceInfo()).getId();
    }

    @Override
    public void deleteUserFace(Long faceId) {
        faceRepository.deleteById(faceId);
    }

    @Override
    public FaceInfoBO detectFace(String imageFile) {
        ImageInfo imageInfo = getImageInfo(imageFile);
        FaceEngine faceEngine = null;
        FaceInfoBO faceInfoBO = new FaceInfoBO();
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<>();
            //人脸检测
            int detectFacesResult = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            int processResult = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(),
                    faceInfoList, FunctionConfiguration.builder().supportAge(true).supportGender(true).build());
            //性别提取
            List<GenderInfo> genderInfoList = new ArrayList<>();
            int genderCode = faceEngine.getGender(genderInfoList);
            Integer gender = Optional.of(genderInfoList).flatMap(list -> list.stream().filter(Objects::nonNull).findFirst()).map(GenderInfo::getGender).orElse(-1);
            faceInfoBO.setGender(gender);
            //年龄提取
            List<AgeInfo> ageInfoList = new ArrayList<>();
            int ageCode = faceEngine.getAge(ageInfoList);
            Integer age = Optional.of(ageInfoList).flatMap(list -> list.stream().filter(Objects::nonNull).findFirst()).map(AgeInfo::getAge).orElse(0);
            faceInfoBO.setAge(age);
            //活性提取
            List<LivenessInfo> livenessInfoList = new ArrayList<>();
            int livenessCode = faceEngine.getLiveness(livenessInfoList);
            Integer liveness = Optional.of(livenessInfoList).flatMap(list -> list.stream().filter(Objects::nonNull).findFirst()).map(LivenessInfo::getLiveness).orElse(-1);
            faceInfoBO.setLiveness(liveness);
            return faceInfoBO;
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return null;
    }

    @Override
    public boolean compareFace(Long userId, String targetImageFile, Float passRate) {
        FaceEngine faceEngine = null;
        ImageInfo imageInfo = getImageInfo(targetImageFile);
        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(extractFaceFeature(imageInfo));
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测得到人脸列表
            //若有1个相似率达到标准则返回true
            List<UserFaceInfo> userFaceInfoList = Optional.of(faceRepository.findAllByUserId(userId)).orElse(new ArrayList<>());
            for (UserFaceInfo userFaceInfo : userFaceInfoList) {
                FaceFeature sourceFaceFeature = new FaceFeature();
                sourceFaceFeature.setFeatureData(userFaceInfo.getFaceFeature());
                FaceSimilar faceSimilar = new FaceSimilar();
                int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                float score = faceSimilar.getScore();
                if (score > (passRate == null ? PASS_RATE : passRate)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return false;
    }

    private byte[] extractFaceFeature(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<>();
            //人脸检测
            int i = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            if (!CollectionUtils.isEmpty(faceInfoList)) {
                FaceFeature faceFeature = new FaceFeature();
                //提取人脸特征
                int resultCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
                return faceFeature.getFeatureData();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return null;
    }

    private ImageInfo getImageInfo(String fileString) {
        byte[] decode = Base64.decode(base64Process(fileString));
        return ImageFactory.getRGBData(decode);
    }

    private String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }
            return base64Str;
        } else {
            return "";
        }
    }


}
