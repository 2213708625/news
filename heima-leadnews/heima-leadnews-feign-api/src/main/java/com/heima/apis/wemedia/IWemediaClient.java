package com.heima.apis.wemedia;

import com.heima.apis.wemedia.fallback.IWemediaClientFallback;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.apis.wemedia
 * @className: IWemediaClient
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/6 19:39
 * @version: 1.0
 */
@FeignClient(value = "leadnews-wemedia",fallback = IWemediaClientFallback.class)
public interface IWemediaClient {

    @GetMapping("/api/v1/channel/list")
    public ResponseResult getChannels();
}
