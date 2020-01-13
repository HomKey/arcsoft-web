package com.hk.arcsoft.repository;

import com.hk.arcsoft.entity.UserFaceInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * Created by HomKey on 2020/1/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FaceRepositoryTest {

    @Autowired
    private FaceRepository faceRepository;

    @Test
    public void findByUserId() {
        List<UserFaceInfo> all = faceRepository.findAll();
        List<UserFaceInfo> byUserId = faceRepository.findAllByUserId(1L);
        System.out.println(byUserId.size());
    }

}