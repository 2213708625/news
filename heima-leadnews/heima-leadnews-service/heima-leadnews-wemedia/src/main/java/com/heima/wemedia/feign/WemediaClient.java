package com.heima.wemedia.feign;

import com.heima.apis.wemedia.IWemediaClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.wemedia.feign
 * @className: WemediaClient
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/6 19:41
 * @version: 1.0
 */
@RestController
public class WemediaClient implements IWemediaClient {

    @Autowired
    private WmChannelService wmChannelService;
    /*
     * @Description:获取所有频道的远程接口
     * @param null
     * @return:
     */
    @GetMapping("/api/v1/channel/list")
    @Override
    public ResponseResult getChannels() {
        return wmChannelService.findAll();
    }
}
