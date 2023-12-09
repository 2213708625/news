package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.apis.article.IArticleClient;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.*;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmChannelMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmSensitiveMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.wemedia.service.impl
 * @className: WmNewsAutoScanServiceImpl
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/11/27 15:52
 * @version: 1.0
 */
@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {
    @Autowired
    private WmNewsMapper wmNewsMapper;
    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    @Autowired
    private WmSensitiveMapper wmSensitiveMapper;

    /**
     * 自媒体文章审核,审核很复杂，我们这里直接修改文章状态
     * @param id  自媒体文章id
     *            status字段：0 草稿  1 待审核  2 审核失败  3 人工审核  4 人工审核通过  8 审核通过（待发布） 9 已发布
     */
    @Override
    @Async
    public void autoScanWmNews(Integer id) {
        //这里停1秒，因为前面保存的可能有点慢，这里是采用异步的，前面数据库还没保存就查，这边直接异常了
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        //通过id从数据库中查询文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if(wmNews==null){//检查参数
            throw new RuntimeException("文章不存在");
        }
        //获取文本map
        Map<String, Object> map = handleText(wmNews);
       //获取文本信息
        String content = (String) map.get("content");
        //只有待审核的文章需要审核
        if(wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            //我们自己的敏感词审核
            boolean isSensitive = handleSensitiveScan(content,wmNews);
            if(!isSensitive){//如果审核不成功，就直接return出去
                return;
            }
            //通过了我们的敏感词审核
            wmNews.setStatus((short)9);
            ResponseResult responseResult = saveAppArticle(wmNews);
            wmNews.setArticleId((Long) responseResult.getData());
            wmNews.setReason("审核成功");
            wmNewsMapper.updateById(wmNews);
        }

    }
    /*
     * @Description:我们自己敏感词的审核
     * @param content
     * @param wmNews
     * @return: boolean
     */
    private  boolean handleSensitiveScan(String content,WmNews wmNews){
        boolean flag=true;
        //获取所有的敏感词
        List<WmSensitive> wmSensitives = wmSensitiveMapper.selectList(Wrappers.<WmSensitive>lambdaQuery().select(WmSensitive::getSensitives));
        //通过stream流将特定属性的集合转为string类型的集合
        List<String> wmSensitiveStr = wmSensitives.stream().map(WmSensitive::getSensitives).collect(Collectors.toList());
        //初始化敏感词库
        SensitiveWordUtil.initMap(wmSensitiveStr);
        //查看文本中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);
        if(map.size()>0){//map大于0说明文本中有敏感词，修改wmnews的状态为2
            wmNews.setStatus((short)2);
            wmNews.setReason("当前文本中存在违规内容"+map);
            wmNewsMapper.updateById(wmNews);
            flag=false;
        }
        return flag;
    }
/*
 * @Description:提取文本与图片
 * @param wmNews
 * @return: java.util.Map<java.lang.String,java.lang.Object>
 */
    private Map<String,Object> handleText(WmNews wmNews){
        String content = wmNews.getContent();
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNotBlank(content)){
            List<Map> maps = JSON.parseArray(content,Map.class);
            for (Map map : maps) {
                if(map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }
            }
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("content", stringBuilder.toString());
        return resultMap;
    }
    /*
     * @Description:保存app端相关的文章数据
     * @param wmNews
     * @return: com.heima.model.common.dtos.ResponseResult
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {
        ArticleDto articleDto = new ArticleDto();
        //复制属性
        BeanUtils.copyProperties(wmNews,articleDto);
        //文章布局
        articleDto.setLayout(wmNews.getType());
        //将dto对象附上频道名字
        WmChannel wmChannel = wmChannelMapper.selectById(articleDto.getChannelId());
        if(wmChannel!=null){
            articleDto.setChannelName(wmChannel.getName());
        }
        //将dto对象附上作者名字
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser!=null){
            articleDto.setAuthorName(wmUser.getName());
        }
        //设置文章id
        if(wmNews.getArticleId()!=null){
            articleDto.setId(wmNews.getArticleId());
        }
        articleDto.setCreatedTime(new Date());
        ResponseResult responseResult = articleClient.saveArticle(articleDto);
        return responseResult;

    }
}
