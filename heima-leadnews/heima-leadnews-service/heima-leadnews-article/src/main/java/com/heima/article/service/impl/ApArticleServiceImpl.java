package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.*;
import com.heima.article.service.ApArticleService;
import com.heima.common.constants.ArticleConstants;
import com.heima.common.constants.BehaviorConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.dtos.ArticleInfoDto;
import com.heima.model.article.mess.ArticleVisitStreamMess;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>  implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;
    @Autowired
    private ApAuthorMapper apAuthorMapper;
    @Autowired
    private ArticleFreemarkerServiceImpl articleFreemarkerService;
    @Autowired
    private ApCollectionMapper apCollectionMapper;

    private final static  short MAX_PAGE_SIZE = 50;

    /**
     * 加载文章列表
     * @param dto
     * @param type 1 加载更多   2 加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //1.检验参数
        //分页条数的校验
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }
        //分页的值不超过50
        size = Math.min(size,MAX_PAGE_SIZE);


        //校验参数  -->type
        if(!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        //频道参数校验
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        //时间校验
        if(dto.getMaxBehotTime() == null)dto.setMaxBehotTime(new Date());
        if(dto.getMinBehotTime() == null)dto.setMinBehotTime(new Date());

        //2.查询
        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, type);
        //3.结果返回
        return ResponseResult.okResult(articleList);
    }

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    /**
     * 保存app端相关文章
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //1.检查参数
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);
        ApAuthor apAuthor = apAuthorMapper.selectOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getName, dto.getAuthorName()));
        apArticle.setAuthorId(apAuthor.getId());
        apArticle.setLikes(0);
        apArticle.setCollection(0);

        //2.判断是否存在id
        if(dto.getId() == null){
            //2.1 不存在id  保存  文章  文章配置  文章内容

            //保存文章
            save(apArticle);

            //保存配置
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);

            //保存 文章内容
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);

        }else {
            //2.2 存在id   修改  文章  文章内容

            //修改  文章
            updateById(apArticle);

            //修改文章内容
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }
        articleFreemarkerService.buildArticleToMinIO(apArticle, dto.getContent());

        //3.结果返回  文章的id
        return ResponseResult.okResult(apArticle.getId());
    }

    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {

        //0.检查参数
        if (dto == null || dto.getArticleId() == null || dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //{ "isfollow": true, "islike": true,"isunlike": false,"iscollection": true }
        boolean isfollow = false, islike = false, isunlike = false, iscollection = false;

        ApUser user = AppThreadLocalUtil.getUser();
        if(user != null){
            //喜欢行为
            String likeBehaviorJson = (String) cacheService.hGet(BehaviorConstants.LIKE_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
            if(StringUtils.isNotBlank(likeBehaviorJson)){
                islike = true;
            }
            //不喜欢的行为
            String unLikeBehaviorJson = (String) cacheService.hGet(BehaviorConstants.UN_LIKE_BEHAVIOR + dto.getArticleId().toString(), user.getId().toString());
            if(StringUtils.isNotBlank(unLikeBehaviorJson)){
                isunlike = true;
            }
            //是否收藏
            String collctionJson = (String) cacheService.hGet(BehaviorConstants.COLLECTION_BEHAVIOR+user.getId(),dto.getArticleId().toString());
            if(StringUtils.isNotBlank(collctionJson)){
                iscollection = true;
            }

            //是否关注
            Double score = cacheService.zScore(BehaviorConstants.APUSER_FOLLOW_RELATION + user.getId(), dto.getAuthorId().toString());
            System.out.println(score);
            if(score != null){
                isfollow = true;
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isfollow);
        resultMap.put("islike", islike);
        resultMap.put("isunlike", isunlike);
        resultMap.put("iscollection", iscollection);

        return ResponseResult.okResult(resultMap);
    }

    /**加载热点文章
     * @param dto
     * @param loadType
     * @param firstPage
     * @return
     */
    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short loadType, boolean firstPage) {
        //如果是首页，就加载热点文章
        if(firstPage){
            //从缓存中取出计算好的热点文章数据
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            if(StringUtils.isNotBlank(jsonStr)){
                List<HotArticleVo> hotArticleVoList = JSON.parseArray(jsonStr, HotArticleVo.class);
                ResponseResult responseResult = ResponseResult.okResult(hotArticleVoList);
                return responseResult;
            }
        }
        return load(dto,loadType);
    }

    /*
     * @Description: 更新文章的分值，同时更新缓存中的热点文章数据
     * @param null
     * @return:
     */
    @Override
    public void updateScore(ArticleVisitStreamMess mess) {
        //更新文章的阅读，点赞，收藏，评论的数量
        ApArticle apArticle = updateArticle(mess);
        //计算当天文章的分值，当前文章的分值要比总的更高，* 3提高优先级
        Integer score = computeScore(apArticle);
        score=score * 3;
        //替换当前文章对应频道的热点数据
        replaceDataToRedis(apArticle,score,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+apArticle.getChannelId());
        //替换推荐对应的热点数据
        replaceDataToRedis(apArticle,score,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);

    }

    /*
     * @Description:替换当前文章热点数据，并存入到redis中
     * @param apArticle
     * @param score
     * @param s
     * @return: void
     */
    private void replaceDataToRedis(ApArticle apArticle,Integer score,String key){
        String articleListStr = cacheService.get(key);
        if(StringUtils.isNotBlank(articleListStr)){
            List<HotArticleVo> hotArticleVoList = JSON.parseArray(articleListStr, HotArticleVo.class);
            boolean flag =true;
            for (HotArticleVo hotArticleVo : hotArticleVoList) {
                //如果缓存中存在该文章，就修改分值
                if(hotArticleVo.getId().equals(apArticle.getId())){
                    hotArticleVo.setScore(score);
                    flag=false;
                    break;
                }
            }
            //缓存中没有该文章，就找出缓存中分值最小的文章，进行替换
            if(flag){
                if(hotArticleVoList.size()>=30){//如果缓存中，没有该文章，且缓存中30个文章已经满了就替换
                    //我们这里用stream流，对集合里元素的分数进行倒叙排序，
                    hotArticleVoList = hotArticleVoList
                            .stream()
                            .sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
                    //我们找到最后点的元素，也就是分值最小的那个文章
                    HotArticleVo lastHotArticle = hotArticleVoList.get(hotArticleVoList.size() - 1);
                    //判断文章分值
                    if(lastHotArticle.getScore()<score) {//如果该文章小于当前文章的分值
                        //从集合中去除这最后一个元素，将当前文章加入
                        hotArticleVoList.remove(lastHotArticle);
                        HotArticleVo hotArticleVo = new HotArticleVo();
                        BeanUtils.copyProperties(apArticle, hotArticleVo);
                        hotArticleVo.setScore(score);
                        hotArticleVoList.add(hotArticleVo);
                    }
                }else {//如果缓存中，没有该文章，且缓存中30个文章没满，直接新增
                    HotArticleVo hotArticleVo = new HotArticleVo();
                    BeanUtils.copyProperties(apArticle,hotArticleVo);
                    hotArticleVo.setScore(score);
                    hotArticleVoList.add(hotArticleVo);
                }
            }
            //再排序一次，缓存到redis中
            hotArticleVoList = hotArticleVoList
                    .stream()
                    .sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
            cacheService.set(key, JSON.toJSONString(hotArticleVoList));

        }
    }

    /*
     * @Description: 更新文章行为数据
     * @param mess
     * @return: com.heima.model.article.pojos.ApArticle
     */
    private ApArticle updateArticle(ArticleVisitStreamMess mess) {
        ApArticle apArticle = getById(mess.getArticleId());
        apArticle.setCollection((apArticle.getCollection()==null?0:apArticle.getCollection())+mess.getCollect());
        apArticle.setComment((apArticle.getComment()==null?0:apArticle.getComment())+mess.getComment());
        apArticle.setLikes((apArticle.getLikes()==null?0:apArticle.getLikes())+mess.getLike());
        apArticle.setViews((apArticle.getViews()==null?0:apArticle.getViews())+mess.getView());
        updateById(apArticle);
        return apArticle;
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
