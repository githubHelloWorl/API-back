package com.chen.API.controller;

import com.chen.API.common.BaseResponse;
import com.chen.API.common.ErrorCode;
import com.chen.API.common.ResultUtils;
import com.chen.API.exception.BusinessException;
import com.chen.API.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.chen.API.model.entity.InterfaceInfo;
import com.chen.API.model.entity.User;
import com.chen.API.service.InterfaceInfoService;
import com.chen.API.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {
    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newPostId = interfaceInfo.getId();
        return ResultUtils.success(newPostId);
    }
}
