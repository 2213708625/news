package com.heima.model.admin.dtos;

import lombok.Data;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.admin.dtos
 * @className: LikesBehaviorDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/4 18:49
 * @version: 1.0
 */
@Data
public class LikesBehaviorDto {

    private Long articleId;
    private Short operation;
    private Short type;
}
