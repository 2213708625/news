package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.AdChannel;
import com.heima.model.wemedia.dtos.ChannelDto;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
public class WmchannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("/channels")
    public ResponseResult findAll(){
        return wmChannelService.findAll();
    }
    ///api/v1/channel/list频道名称分页模糊查询
    @PostMapping("/list")
    public ResponseResult listChannels(@RequestBody ChannelDto dto){
        return wmChannelService.pageList(dto);
    }
    ///api/v1/channel/save
    @PostMapping("/save")
    public ResponseResult saveChannels(@RequestBody AdChannel adChannel){
        return wmChannelService.saveChannels(adChannel);
    }
    ///api/v1/channel/update
    @PostMapping("/update")
    public ResponseResult updateChannels(@RequestBody AdChannel adChannel){
        return wmChannelService.updateChannels(adChannel);
    }
    ///api/v1/channel/del/{id}
    @GetMapping("/del/{id}")
    public ResponseResult deleteChannels(@PathVariable("id") Integer id){
        return wmChannelService.delChannels(id);
    }


}
