package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 条件查询文章列表
     * @param dto
     * @return
     */
    public ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 发布修改文章或保存为草稿
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    /**
     * 文章的上下架
     * @param dto
     * @return
     */
    public ResponseResult downOrUp(WmNewsDto dto);

    /*
     * @Description:分页查询文章列表
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult listVo(NewsAuthDto dto);

    /*
     * @Description:人工审核
     * @param null
     * @return:
     */
    public ResponseResult oneVo(Integer id);

    /*
     * @Description: 人工审核失败
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult failAuth(NewsAuthDto dto);

    /*
     * @Description:人工审核成功
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult passAuth(NewsAuthDto dto);
}
