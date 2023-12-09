package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.wemedia.dtos
 * @className: AuthDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 18:35
 * @version: 1.0
 */
@Data
public class AuthDto extends PageRequestDto {

    private Integer id;
    private String msg;
    private Integer status;
}
