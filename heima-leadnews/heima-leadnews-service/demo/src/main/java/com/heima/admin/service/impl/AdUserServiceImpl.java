package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDto;
import com.heima.model.admin.pojos.AdUser;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import com.heima.admin.mapper.AdUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.wemedia.service.impl
 * @className: AdUserServiceImpl
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 8:15
 * @version: 1.0
 */
@Service
@Slf4j
@Transactional
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements AdUserService {

    /**
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(AdUserDto dto) {
        //校验参数
        if(StringUtils.isBlank(dto.getName())||StringUtils.isBlank(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //查询用户
        AdUser adUser = getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, dto.getName()));
        if(adUser==null){
            return ResponseResult.okResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //比对密码
        String salt = adUser.getSalt();
        String pswd = dto.getPassword();
        pswd= DigestUtils.md5DigestAsHex((pswd + salt).getBytes());

        if(pswd.equals(adUser.getPassword())){
            //返回token
            Map<String,Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(adUser.getId().longValue()));
            adUser.setSalt("");
            adUser.setPassword("");
            map.put("user", adUser);
            return ResponseResult.okResult(map);
        }else {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
    }
}
