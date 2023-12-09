package com.heima.apis.wemedia.fallback;

import com.heima.apis.wemedia.IWemediaClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.apis.wemedia.fallback
 * @className: IWemediaClientFallback
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/6 19:40
 * @version: 1.0
 */
public class IWemediaClientFallback implements IWemediaClient {
    /**
     * @return
     */
    @Override
    public ResponseResult getChannels() {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
