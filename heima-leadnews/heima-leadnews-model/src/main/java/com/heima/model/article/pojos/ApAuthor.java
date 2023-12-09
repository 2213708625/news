package com.heima.model.article.pojos;

import lombok.Data;

import java.util.Date;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.article.pojos
 * @className: ApAuthor
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/5 18:16
 * @version: 1.0
 */
@Data
public class ApAuthor {
    private Integer id;
    private String name;
    private Short type;
    private Integer userId;
    private Date createdTime;
    private Integer wmUserId;

}
