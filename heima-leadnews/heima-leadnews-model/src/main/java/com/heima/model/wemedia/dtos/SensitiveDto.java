package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.admin.dtos
 * @className: SensitiveDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 13:04
 * @version: 1.0
 */
@Data
public class SensitiveDto extends PageRequestDto {
    private String name;
}
