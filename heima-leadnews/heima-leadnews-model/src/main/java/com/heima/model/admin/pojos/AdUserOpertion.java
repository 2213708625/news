package com.heima.model.admin.pojos;

import java.util.Date;
import lombok.Data;

/**
    * 管理员操作行为信息表
    */
@Data
public class AdUserOpertion {
    private Long id;

    /**
    * 用户ID
    */
    private Integer userId;

    /**
    * 登录设备ID
    */
    private Integer equipmentId;

    /**
    * 登录IP
    */
    private String ip;

    /**
    * 登录地址
    */
    private String address;

    /**
    * 操作类型
    */
    private Integer type;

    /**
    * 操作描述
    */
    private String description;

    /**
    * 登录时间
    */
    private Date createdTime;
}
