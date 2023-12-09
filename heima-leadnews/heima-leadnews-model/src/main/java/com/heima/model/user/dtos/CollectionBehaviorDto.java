package com.heima.model.user.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.user.dtos
 * @className: CollectionBehaviorDto
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/5 9:37
 * @version: 1.0
 */
@Data
public class CollectionBehaviorDto {
    private Long entryId;
    private Short operation;
    private Date publishedTime;
    private Short type;


}
