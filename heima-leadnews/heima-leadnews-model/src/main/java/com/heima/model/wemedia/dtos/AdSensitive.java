package com.heima.model.wemedia.dtos;

import lombok.Data;

import java.net.Inet4Address;
import java.util.Date;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.model.wemedia.dtos
 * @className: AdSensitive
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/3 14:37
 * @version: 1.0
 */
@Data
public class AdSensitive {
    private Integer id;
    private String sensitives;
    private Date createdTime;
}
