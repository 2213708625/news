package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.AdSensitive;
import com.heima.model.wemedia.dtos.SensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmSensitiveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.wemedia.service.impl
 * @className: WmSensitiveServiceImpl
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 13:22
 * @version: 1.0
 */
@Service
@Slf4j
@Transactional
public class WmSensitiveServiceImpl extends ServiceImpl<WmSensitiveMapper, WmSensitive>implements WmSensitiveService {

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * @param dto 分页展示敏感词列表
     * @return
     */
    @Override
    public ResponseResult listSensitive(SensitiveDto dto) {
        //校验参数
        dto.checkParam();

        /*- 查询需要按照创建时间倒序查询
          - 按照敏感词名称模糊查询
          - 分页查询
         */
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmSensitive> lambdaQueryWrapper = new LambdaQueryWrapper();
        //按照敏感词模糊查询
        if(dto.getName()!=null && StringUtils.isNotBlank(dto.getName())){
            lambdaQueryWrapper.like(WmSensitive::getSensitives,dto.getName());
        }
        //按照时间倒叙查询
        lambdaQueryWrapper.orderByDesc(WmSensitive::getCreatedTime);

        //封装结果返回
        page = page(page,lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(),dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * @param id 根据id删除敏感词
     * @return
     */
    @Override
    public ResponseResult delSensitive(Integer id) {
        int i = wmSensitiveMapper.deleteById(id);
        if(i>0){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**添加敏感词
     * @param adSensitive
     * @return
     */
    @Override
    public ResponseResult saveSensitives(AdSensitive adSensitive) {
        //查询判断该敏感词是否存在
        WmSensitive wmSensitive = wmSensitiveMapper.selectOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, adSensitive.getSensitives()));
        if(wmSensitive!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"该敏感词已经存在");
        }
        //敏感词不存在就新增
        wmSensitive = new WmSensitive();
        BeanUtils.copyProperties(adSensitive,wmSensitive);
        wmSensitive.setCreatedTime(new Date());
        boolean flag = save(wmSensitive);
        if(flag){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**
     * @param adSensitive 修改敏感词
     * @return
     */
    @Override
    public ResponseResult updateSensitives(AdSensitive adSensitive) {
        //判断你当前修改的敏感词是否已经存在
        WmSensitive wmSensitive = wmSensitiveMapper.selectOne(Wrappers.<WmSensitive>lambdaQuery().eq(WmSensitive::getSensitives, adSensitive.getSensitives()));
        if(wmSensitive!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"当前修改的敏感词已经存在");
        }
        //修改的敏感词在当前数据库内不存在，修改敏感词
        boolean flag = update(Wrappers.<WmSensitive>lambdaUpdate().eq(WmSensitive::getId, adSensitive.getId())
                .set(WmSensitive::getSensitives, adSensitive.getSensitives()));
        if(flag){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
