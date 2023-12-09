package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.AdSensitive;
import com.heima.model.wemedia.dtos.SensitiveDto;
import com.heima.model.wemedia.pojos.WmSensitive;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.admin.service
 * @className: WmSensitiveService
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 13:20
 * @version: 1.0
 */
public interface WmSensitiveService extends IService<WmSensitive> {


    /*
     * @Description:展示所有敏感词
     * @param dto
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult listSensitive(SensitiveDto dto);
    /*
     * @Description:删除敏感词
     * @param null
     * @return:
     */
    public ResponseResult delSensitive(Integer id);
    /*
     * @Description: 添加敏感词
     * @param adSensitive
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    public ResponseResult saveSensitives(AdSensitive adSensitive);
    /*
     * @Description: 修改敏感词
     * @param null
     * @return:
     */
    public ResponseResult updateSensitives(AdSensitive adSensitive);
}
