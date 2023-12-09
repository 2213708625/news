package com.heima.model.wemedia.dtos;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.wemedia.dtos
 * @className: NewsAuthDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/4 12:24
 * @version: 1.0
 */

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

@Data
public class NewsAuthDto extends PageRequestDto {

    private Integer id;
    private String msg;
    private Integer status;
    private String title;

}
