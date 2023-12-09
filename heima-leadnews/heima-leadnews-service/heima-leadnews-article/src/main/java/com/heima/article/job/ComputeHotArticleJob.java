package com.heima.article.job;

import com.heima.article.service.HotArticleService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.article.job
 * @className: ComputeHotArticleJob
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/7 8:48
 * @version: 1.0
 */
@Component
@Slf4j
public class ComputeHotArticleJob {
    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("computeHotArticleJob")
    public void handler(){
        log.info("热文章计算分值调度任务开始");
        hotArticleService.computeHotArticle();
        log.info("热文章计算分值调度任务结束");
    }
}
