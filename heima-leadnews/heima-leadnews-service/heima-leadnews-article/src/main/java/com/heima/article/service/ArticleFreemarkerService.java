package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.article.service
 * @className: ArticleFreemarkerService
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/2 8:13
 * @version: 1.0
 */
public interface ArticleFreemarkerService {

    public void buildArticleToMinIO(ApArticle apArticle, String content);

}
