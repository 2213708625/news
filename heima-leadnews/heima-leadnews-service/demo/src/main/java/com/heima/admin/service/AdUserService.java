package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.wemedia.service
 * @className: AdUserService
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 8:14
 * @version: 1.0
 */
public interface AdUserService extends IService<AdUser> {

    /*
     * @Description:登录
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult login(AdUserDto dto);
}
