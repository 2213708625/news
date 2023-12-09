package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.UserStatusConstans;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.model.wemedia.dtos.AuthDto;
import com.heima.user.mapper.ApUserRealNameMapper;
import com.heima.user.service.ApAuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.user.service.impl
 * @className: ApAuthServiceImpl
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 19:03
 * @version: 1.0
 */
@Service
@Slf4j
@Transactional
public class ApAuthServiceImpl extends ServiceImpl<ApUserRealNameMapper, ApUserRealname> implements ApAuthService {
    /**
     * @param dto分页查询用户审核
     * @return
     */
    @Override
    public ResponseResult listApAuth(AuthDto dto) {
        //校验参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //分页查询
        IPage page = new Page(dto.getPage(),dto.getSize());
        //条件status分类查询
        LambdaQueryWrapper<ApUserRealname> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(dto.getStatus()!=null){
            lambdaQueryWrapper.eq(ApUserRealname::getStatus,dto.getStatus());
        }
        lambdaQueryWrapper.orderByDesc(ApUserRealname::getCreatedTime);
        //返回参数
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult =new PageResponseResult(dto.getPage(),dto.getSize(),(int)page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**审核失败，通过id修改状态，status改为2
     * @param dto
     * @return
     */
    @Override
    public ResponseResult failApAuth(AuthDto dto) {
        //校验参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //审核失败，修改状态为2
        boolean flag = update(Wrappers.<ApUserRealname>lambdaUpdate().eq(ApUserRealname::getId, dto.getId()).set(ApUserRealname::getStatus, UserStatusConstans.FAIL_AUTH)
                .set(ApUserRealname::getReason, dto.getMsg()));
        if(flag){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"状态修改失败");
    }

    /**审核通过，修改status为9
     * @param dto
     * @return
     */
    @Override
    public ResponseResult passApAuth(AuthDto dto) {
        //校验参数
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //审核成功，修改状态为9
        boolean flag = update(Wrappers.<ApUserRealname>lambdaUpdate().eq(ApUserRealname::getId, dto.getId()).set(ApUserRealname::getStatus, UserStatusConstans.PASS_AUTH));
        if(flag){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"状态修改失败");
    }
}
