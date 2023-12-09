package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.wemedia.dtos
 * @className: AdChannel
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 16:05
 * @version: 1.0
 */
@Data
public class AdChannel {

    private Integer id;
    private String description;
    private Boolean isDefault;
    private String name;
    private Integer ord;
    private Boolean status;
    private Date createdTime;



}
