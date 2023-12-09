package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.AdSensitive;
import com.heima.model.wemedia.dtos.SensitiveDto;
import com.heima.wemedia.service.WmSensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.admin.controller.v1
 * @className: WmSensitiveController
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 13:18
 * @version: 1.0
 */
@RestController
@RequestMapping("/api/v1/sensitive")
public class WmSensitiveController {
    @Autowired
    private WmSensitiveService wmSensitiveService;

    ///api/v1/sensitive/list
    @PostMapping("/list")
    public ResponseResult listSensitive(@RequestBody SensitiveDto dto){
        return wmSensitiveService.listSensitive(dto);
    }

    ///api/v1/sensitive/del/{id}
    @DeleteMapping("/del/{id}")
    public ResponseResult delSensitive(@PathVariable("id") Integer id){
        return wmSensitiveService.delSensitive(id);
    }

    ///api/v1/sensitive/save
    @PostMapping("/save")
    public ResponseResult saveSensitive(@RequestBody AdSensitive adSensitive){
        return wmSensitiveService.saveSensitives(adSensitive);
    }
    ///api/v1/sensitive/update
    @PostMapping("/update")
    public ResponseResult updateSensitive(@RequestBody AdSensitive adSensitive){
        return wmSensitiveService.updateSensitives(adSensitive);
    }
}
