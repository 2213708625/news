package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.AuthDto;
import com.heima.user.service.ApAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.user.controller.v1
 * @className: ApAuthController
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 18:57
 * @version: 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
public class ApAuthController {

    @Autowired
    private ApAuthService apAuthService;
    ///api/v1/auth/list
    @PostMapping("/list")
    public ResponseResult listApAuth(@RequestBody AuthDto dto){
        return apAuthService.listApAuth(dto);
    }
    ///api/v1/auth/authFail
    @PostMapping("/authFail")
    public ResponseResult failApAuth(@RequestBody AuthDto dto){
        return apAuthService.failApAuth(dto);
    }

    ///api/v1/auth/authPass
    @PostMapping("/authPass")
    public ResponseResult passApAuth(@RequestBody AuthDto dto){
        return apAuthService.passApAuth(dto);
    }

}
