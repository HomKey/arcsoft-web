package com.hk.arcsoft.core.factory;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.hk.arcsoft.enums.ArcSoftErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by HomKey on 2020/1/8.
 */
@Slf4j
public class FaceEngineFactory extends BasePooledObjectFactory<FaceEngine> {

    private String appId;
    private String sdkKey;
    private String sdkLibPath;
    private EngineConfiguration engineConfiguration;

    public FaceEngineFactory(String appId, String sdkKey, String sdkLibPath, EngineConfiguration engineConfiguration) {
        this.appId = appId;
        this.sdkKey = sdkKey;
        this.sdkLibPath = sdkLibPath;
        this.engineConfiguration = engineConfiguration;
    }


    @Override
    public FaceEngine create() throws Exception {
        FaceEngine faceEngine = new FaceEngine(sdkLibPath);
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        // 引擎激活
        log.info("引擎激活:" + ArcSoftErrorCodeEnum.getDescriptionByCode(activeCode).getDescription());
        int initCode = faceEngine.init(engineConfiguration);
        // 获取激活文件信息
        log.info("获取激活文件信息:" + ArcSoftErrorCodeEnum.getDescriptionByCode(initCode).getDescription());
        faceEngine.setLivenessParam(0.5f, 0.7f);
        return faceEngine;
    }

    @Override
    public PooledObject<FaceEngine> wrap(FaceEngine faceEngine) {
        return new DefaultPooledObject<>(faceEngine);
    }

    @Override
    public void destroyObject(PooledObject<FaceEngine> pooledObject) throws Exception {
        FaceEngine faceEngine = pooledObject.getObject();
        int unInitCode = faceEngine.unInit();
        // 卸载引擎
        log.debug("卸载引擎:" + ArcSoftErrorCodeEnum.getDescriptionByCode(unInitCode).getDescription());
        super.destroyObject(pooledObject);
    }
}
