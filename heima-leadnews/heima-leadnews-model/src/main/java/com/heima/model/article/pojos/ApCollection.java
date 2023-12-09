package com.heima.model.article.pojos;

import java.util.Date;
import lombok.Data;

/**
    * APP收藏信息表
    */
@Data
public class ApCollection {
    private Long id;

    /**
    * 实体ID
    */
    private Integer entryId;

    /**
    * 文章ID
    */
    private Long articleId;

    /**
    * 点赞内容类型
            0文章
            1动态
    */
    private Short type;

    /**
    * 创建时间
    */
    private Date collectionTime;

    /**
    * 发布时间
    */
    private Date publishedTime;
}
