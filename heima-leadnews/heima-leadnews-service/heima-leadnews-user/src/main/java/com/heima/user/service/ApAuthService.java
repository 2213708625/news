package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.model.wemedia.dtos.AuthDto;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.user.service
 * @className: ApAuthService
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 18:59
 * @version: 1.0
 */
public interface ApAuthService extends IService<ApUserRealname> {


    /*
     * @Description:分页查询用户审核
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult listApAuth(AuthDto dto);
    /*
     * @Description:审核失败
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult failApAuth(AuthDto dto);

    /*
     * @Description:审核通过
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult passApAuth(AuthDto dto);
}
