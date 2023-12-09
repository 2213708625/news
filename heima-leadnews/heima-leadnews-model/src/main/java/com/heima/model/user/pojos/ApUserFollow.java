package com.heima.model.user.pojos;

import java.util.Date;
import lombok.Data;

/**
    * APP用户关注信息表
    */
@Data
public class ApUserFollow {
    /**
    * 主键
    */
    private Long id;

    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 关注作者ID
    */
    private Long followId;

    /**
    * 粉丝昵称
    */
    private String followName;

    /**
    * 关注度
            0 偶尔感兴趣
            1 一般
            2 经常
            3 高度
    */
    private Byte level;

    /**
    * 是否动态通知
    */
    private Byte isNotice;

    /**
    * 创建时间
    */
    private Date createdTime;
}
