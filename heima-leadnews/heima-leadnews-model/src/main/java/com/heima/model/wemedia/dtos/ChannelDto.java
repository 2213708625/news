package com.heima.model.wemedia.dtos;

import com.heima.model.common.dtos.PageRequestDto;
import lombok.Data;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.wemedia.dtos
 * @className: ChannelDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 15:27
 * @version: 1.0
 */
@Data
public class ChannelDto extends PageRequestDto {
    private String name;

}
