package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.AdChannel;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.service.WmChannelService;
import jdk.nashorn.internal.objects.annotations.Where;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.WrappedFileWatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {
    @Autowired
    private WmChannelMapper wmChannelMapper;

    /**
     * 查询所有频道
     *
     * @return
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }

    /**
     * 分页模糊查询所有频道
     *
     * @return
     */
    @Override
    public ResponseResult pageList(ChannelDto dto) {
        //校验参数
        dto.checkParam();
        //分页模糊查询
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmChannel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(dto.getName()) && dto.getName() != null) {
            lambdaQueryWrapper.like(WmChannel::getName, dto.getName());
        }
        //按照创建时间倒叙
        lambdaQueryWrapper.orderByDesc(WmChannel::getCreatedTime);
        page = page(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());
        return responseResult;
    }

    /**
     * @param adChannel 新增频道
     * @return {
     * "name": "岁数大",
     * "status": true,
     * "description": "但撒",
     * "ord": 1
     * }
     */
    @Override
    public ResponseResult saveChannels(AdChannel adChannel) {
        //校验参数
        if (adChannel.getName() == null && StringUtils.isBlank(adChannel.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //判断表内是否已有该频道
        WmChannel wmChannel = wmChannelMapper.selectOne(Wrappers.<WmChannel>lambdaQuery().eq(WmChannel::getName, adChannel.getName()));
        if (wmChannel != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "该频道已存在");
        }
        //保存频道
        wmChannel = new WmChannel();
        BeanUtils.copyProperties(adChannel, wmChannel);
        wmChannel.setCreatedTime(new Date());
        wmChannel.setIsDefault(true);
        boolean flag = save(wmChannel);
        if (flag) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**
     * @param adChannel修改频道
     * @return
     */
    @Override
    public ResponseResult updateChannels(AdChannel adChannel) {
        //校验参数
        if(adChannel.getName()==null &&StringUtils.isBlank(adChannel.getName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //修改频道
        WmChannel wmChannel = new WmChannel();
        BeanUtils.copyProperties(adChannel,wmChannel);
        boolean flag = updateById(wmChannel);
        if(flag){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**根据id删除频道
     * @param id
     * @return
     */
    @Override
    public ResponseResult delChannels(Integer id) {
        //校验参数
         if(id==null){
             return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
         }
        //删除频道只有禁用的才能删除，status=0
        Map<String,Object> map = new HashMap<>();
        map.put("status",0);
        map.put("id", id);
        int flag = wmChannelMapper.deleteByMap(map);
        if(flag>0){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"该频道正在启用中");
    }
}
