package com.heima.wemedia.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.wemedia.service.WmNewsService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/news")
public class WmNewsController {

    @Autowired
    private WmNewsService wmNewsService;

    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmNewsPageReqDto dto){
        return wmNewsService.findList(dto);
    }

    @PostMapping("/submit")
    public ResponseResult submitNews(@RequestBody WmNewsDto dto){
        return wmNewsService.submitNews(dto);
    }

    @PostMapping("/down_or_up")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto){
        return wmNewsService.downOrUp(dto);
    }
    ///api/v1/news/list_vo
    @PostMapping("/list_vo")
    public ResponseResult listVo(@RequestBody NewsAuthDto dto){
        return wmNewsService.listVo(dto);
    }

    ///api/v1/news/one_vo/{id}`
    @GetMapping("/one_vo/{id}")
    public ResponseResult oneVo(@PathVariable("id") Integer id){
       return wmNewsService.oneVo(id);
    }
    ///api/v1/news/auth_fail
    @PostMapping("auth_fail")
    public ResponseResult failAuth(@RequestBody NewsAuthDto dto){
        return wmNewsService.failAuth(dto);
    }
    ///api/v1/news/auth_pass
    @PostMapping("/auth_pass")
    public ResponseResult passAuth(@RequestBody NewsAuthDto dto){
        return wmNewsService.passAuth(dto);
    }
}
