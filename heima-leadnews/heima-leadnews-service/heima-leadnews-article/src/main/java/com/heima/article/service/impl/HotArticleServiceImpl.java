package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.wemedia.IWemediaClient;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.article.service.impl
 * @className: HotArticleServiceImpl
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/6 19:00
 * @version: 1.0
 */
@Service
@Slf4j
@Transactional
public class HotArticleServiceImpl implements HotArticleService {
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private IWemediaClient wemediaClient;
    @Autowired
    private CacheService cacheService;
    /**
     *
     */
    @Override
    public void computeHotArticle() {
        //查询前五天的文章
        Date dateParam = DateTime.now().minusDays(5).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5days(dateParam);

        //计算文章的权重分值
        List<HotArticleVo> hotArticleVoList = computeHotArticleScore(apArticleList);
        //为每个频道缓存30条分值较高的文章
        cacheTagToRedis(hotArticleVoList);

    }


    /*
     * @Description: 为每个频道缓存30条分值较高的文章
     * @param hotArticleVoList
     * @return: void
     */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVoList) {
        //为每个频道缓存30条分值较高的文章
        //获取所有频道
        ResponseResult responseResult = wemediaClient.getChannels();
        if(responseResult.getCode().equals(200)){
            Object channelData = responseResult.getData();
            String channeljsonString = JSON.toJSONString(channelData);
            List<WmChannel> wmChannels = JSON.parseArray(channeljsonString, WmChannel.class);
            //检索出每个频道的文章
            if(wmChannels!=null && wmChannels.size()>0){
                for (WmChannel wmChannel : wmChannels) {
                    //将hotArticleVoList中是当前channel的文章过滤出来
                    List<HotArticleVo> hotArticleVos = hotArticleVoList.stream().filter(x -> x.getChannelId().equals(wmChannel.getId())).collect(Collectors.toList());
                    //将当前频道的所有文章通过分数排序，并加入缓存
                    sortAndCache(hotArticleVos,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+wmChannel.getId());
                }
            }
        }
        //将全部通过分数排序，这个是推荐频道的文章缓存
        sortAndCache(hotArticleVoList,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);

    }

    /*
     * @Description:将集合通过分数进行倒叙排序，且取前30条数据加入缓存
     * @param null
     * @return:
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVoList,String key){
        hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
        //取30条
        if(hotArticleVoList.size()>30){
            hotArticleVoList=hotArticleVoList.subList(0,30);
        }
        //加入缓存
        cacheService.set(key, JSON.toJSONString(hotArticleVoList));

    }

    /*
     * @Description:计算每个文章的权重分值的逻辑方法
     * @param apArticleList
     * @return: java.util.List<com.heima.model.article.vos.HotArticleVo>
     */
    private List<HotArticleVo> computeHotArticleScore(List<ApArticle> apArticleList) {
        List<HotArticleVo> hotArticleVoList = new ArrayList<>();
        if(apArticleList!=null && apArticleList.size()>0){
            for (ApArticle apArticle : apArticleList) {
                //拷贝属性
                HotArticleVo hotArticleVo = new HotArticleVo();
                BeanUtils.copyProperties(apArticle,hotArticleVo);
                //计算aparticle文章热度权重
                Integer score = computeScore(apArticle);
                hotArticleVo.setScore(score);
                hotArticleVoList.add(hotArticleVo);
            }
        }
        return hotArticleVoList;
    }

    /*
     * @Description: 计算文章的权重分值
     * @param apArticle
     * @return: java.lang.Integer
     */
    private Integer computeScore(ApArticle apArticle) {
        //定义一个score返回权重分值
        Integer score = 0;
        //添加文章喜欢权重
        if(apArticle.getLikes()!=null){
            score+=apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        //添加文章收藏权重
        if(apArticle.getCollection()!=null){
            score+=apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        //添加文章阅读量权重
        if(apArticle.getViews()!=null){
            score+=apArticle.getViews();
        }
        //添加文章评论权重
        if(apArticle.getComment()!=null){
            score+=apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        return score;
    }


}
