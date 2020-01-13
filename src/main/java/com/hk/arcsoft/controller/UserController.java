package com.hk.arcsoft.controller;

import com.hk.arcsoft.bo.FaceInfoBO;
import com.hk.arcsoft.bo.UserInfoBO;
import com.hk.arcsoft.dto.FaceInfoDTO;
import com.hk.arcsoft.dto.UserInfoDTO;
import com.hk.arcsoft.dto.UserInfoWithPwdDTO;
import com.hk.arcsoft.dto.common.ResponseResult;
import com.hk.arcsoft.enums.common.ResponseEnum;
import com.hk.arcsoft.service.FaceService;
import com.hk.arcsoft.service.UserService;
import com.hk.arcsoft.service.impl.ArcSoftFaceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by HomKey on 2020/1/9.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource(name = "arcSoftFaceServiceImpl")
    private FaceService faceService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseResult getUsers(
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        Page<UserInfoBO> userInfos = userService.findAllByPage(page, pageSize);
        List<UserInfoDTO> resultList = new ArrayList<>();
        for (UserInfoBO userInfoBo : userInfos.getContent()) {
            UserInfoDTO userInfoDTO = UserInfoDTO.convertFor(userInfoBo);
            resultList.add(userInfoDTO);
        }
        Page<UserInfoDTO> userInfoDto = new PageImpl<>(resultList, userInfos.getPageable(), userInfos.getTotalElements());
        return new ResponseResult(ResponseEnum.SUCCESS, userInfoDto);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseResult addUser(UserInfoWithPwdDTO userInfoDTO) {
        UserInfoBO userInfoBo = userInfoDTO.convertToUserInfoBO();
        if (userInfoBo == null) {
            return new ResponseResult(ResponseEnum.ERROR, "用户信息输入不符合要求");
        }
        userInfoBo.setId(null);
        userInfoBo = userService.save(userInfoBo);
        UserInfoDTO result = UserInfoDTO.convertFor(userInfoBo);
        return new ResponseResult(ResponseEnum.SUCCESS, result);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseResult editUser(UserInfoWithPwdDTO userInfoDTO) {
        UserInfoBO userInfoBo = userInfoDTO.convertToUserInfoBO();
        if (userInfoBo == null || userInfoBo.getId() == null) {
            return new ResponseResult(ResponseEnum.ERROR, "用户信息输入不符合要求");
        }
        userInfoBo = userService.save(userInfoBo);
        UserInfoDTO result = UserInfoDTO.convertFor(userInfoBo);
        return new ResponseResult(ResponseEnum.SUCCESS, result);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseResult deleteUser(@PathVariable(name = "userId") Long userId) {
        userService.delete(userId);
        return new ResponseResult(ResponseEnum.SUCCESS, userId);
    }

    @RequestMapping(value = "/face", method = RequestMethod.POST)
    public ResponseResult addFace(@RequestParam("imageFile") String imageFile, @RequestParam("userId") Long userId) {
        Long faceId = faceService.addUserFace(userId, imageFile);
        if (faceId == null) {
            return new ResponseResult(ResponseEnum.ERROR, "图片无法识别");
        }
        FaceInfoBO faceInfoBO = faceService.detectFace(imageFile);
        FaceInfoDTO faceInfoDTO = FaceInfoDTO.convertFor(faceInfoBO);
        faceInfoDTO.setId(faceId);
        return new ResponseResult(ResponseEnum.SUCCESS, faceInfoDTO);
    }

    @RequestMapping(value = "/face/{faceId}", method = RequestMethod.DELETE)
    public ResponseResult delFace(@PathVariable(name = "faceId") Long faceId) {
        faceService.deleteUserFace(faceId);
        return new ResponseResult(ResponseEnum.SUCCESS, faceId);
    }

    @RequestMapping(value = "/face/check", method = RequestMethod.POST)
    public ResponseResult checkUserByFace(@RequestParam("imageFile") String imageFile, @RequestParam("userId") Long userId) {
        boolean result = faceService.compareFace(userId, imageFile, ArcSoftFaceServiceImpl.PASS_RATE);
        if (result) {
            return new ResponseResult(ResponseEnum.SUCCESS);
        } else {
            return new ResponseResult(ResponseEnum.ERROR);
        }
    }

}
