package com.heima.apis.schedule.fallback;

import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.schedule.dtos.Task;
import org.springframework.stereotype.Component;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.apis.schedule.fallback
 * @className: IScheduleClientfallback
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/1 18:50
 * @version: 1.0
 */
@Component
public class IScheduleClientfallback implements IScheduleClient {
    @Override
    public ResponseResult addTask(Task task) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult cancelTask(long taskId) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    @Override
    public ResponseResult poll(int type, int priority) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
