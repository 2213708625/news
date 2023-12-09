package com.heima.model.user.pojos;

import java.util.Date;
import lombok.Data;

/**
    * APP用户粉丝信息表
    */
@Data
public class ApUserFan {
    /**
    * 主键
    */
    private Long id;

    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 粉丝ID
    */
    private Long fansId;

    /**
    * 粉丝昵称
    */
    private String fansName;

    /**
    * 粉丝忠实度
            0 正常
            1 潜力股
            2 勇士
            3 铁杆
            4 老铁
    */
    private Byte level;

    /**
    * 创建时间
    */
    private Date createdTime;

    /**
    * 是否可见我动态
    */
    private Byte isDisplay;

    /**
    * 是否屏蔽私信
    */
    private Byte isShieldLetter;

    /**
    * 是否屏蔽评论
    */
    private Byte isShieldComment;
}
