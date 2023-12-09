package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.AdChannel;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 查询所有频道
     * @return
     */
    public ResponseResult findAll();
    /*
     * @Description:分页模糊查询所有频道
     * @param
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult pageList(ChannelDto dto);
    /*
     * @Description:新增频道
     * @param null
     * @return:
     */
    public ResponseResult saveChannels(AdChannel adChannel);
    /*
     * @Description:修改频道
     * @param adChannel
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult updateChannels(AdChannel adChannel);

    /*
     * @Description: 根据id删除频道
     * @param id
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult delChannels(Integer id);

}
