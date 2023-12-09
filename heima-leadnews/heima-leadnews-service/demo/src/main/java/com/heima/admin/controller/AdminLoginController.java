package com.heima.admin.controller;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.admin.controller
 * @className: AdminLoginController
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 12:23
 * @version: 1.0
 */
@RestController
@RequestMapping("/login")
public class AdminLoginController {

    @Autowired
    private AdUserService adUserService;



    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUserDto dto){
        return adUserService.login(dto);
    }
}
