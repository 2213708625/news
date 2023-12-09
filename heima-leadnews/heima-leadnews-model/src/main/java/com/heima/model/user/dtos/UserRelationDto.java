package com.heima.model.user.dtos;

import lombok.Data;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.user.dtos
 * @className: UserRelationDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/5 9:35
 * @version: 1.0
 */
@Data
public class UserRelationDto {

    private Long articleId;
    private Integer authorId;
    private Short operation;

}
