package com.heima.search.service.impl;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import com.heima.search.dtos.HistorySearchDto;
import com.heima.search.pojos.ApUserSearch;
import com.heima.search.service.ApUserSearchService;
import com.heima.utils.thread.AppThreadLocalUtil;
import javafx.beans.binding.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @projectName: heima-leadnews
 * @package: com.heima.search.service.im
 * @className: ApUserSearchServiceImpl
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/2 9:24
 * @version: 1.0
  **/
@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 保存用户搜索历史记录
     * @param keyword
     * @param userId
     */
    @Async
    @Override
    public void insert(String keyword, Integer userId) {

        //1.查询当前用户的搜索关键词
        Query query = Query.query(Criteria.where("userId").is(userId).and("keyword").is(keyword));
        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        //2.存在 更新创建时间
        if(apUserSearch!=null){
            apUserSearch.setCreatedTime(new Date());
            mongoTemplate.save(apUserSearch);
            log.info("存在该关键词，修改时间");
        }

        //3.不存在，通过当前用户id获取关键词集合，然后通过该判断当前历史记录总数量是否超过10
        //新创建对象，做新建处理
        apUserSearch = new ApUserSearch();
        apUserSearch.setUserId(userId);
        apUserSearch.setKeyword(keyword);
        apUserSearch.setCreatedTime(new Date());
        //通过当前userId获取关键字集合
        Query query1 = Query.query(Criteria.where("userId").is(userId));
        //按时间进行倒叙排序的集合
        query1.with(Sort.by(Sort.Direction.DESC,"createTime"));
        List<ApUserSearch> apUserSearchList = mongoTemplate.find(query1, ApUserSearch.class);
        //小于10直接插入
        if(apUserSearchList ==null || apUserSearchList.size()<10){
            mongoTemplate.save(apUserSearch);
        }else {
            ApUserSearch lastUserSearch = apUserSearchList.get(apUserSearchList.size() - 1);
            mongoTemplate.findAndReplace(Query.query(Criteria.where("id").is(lastUserSearch.getId())),apUserSearch);
        }

    }

    /**
     * 查询搜索历史
     *
     * @return
     */
    @Override
    public ResponseResult findUserSearch() {
        ApUser apUser = AppThreadLocalUtil.getUser();
        if(apUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        List<ApUserSearch> apUserSearchList = mongoTemplate.find(Query.query(Criteria.where("userId").is(apUser.getId())).with(Sort.by(Sort.Direction.DESC, "createTime")), ApUserSearch.class);
        return ResponseResult.okResult(apUserSearchList);
    }
    /**
     删除搜索历史
     @param historySearchDto
     @return
     */
    @Override
    public ResponseResult delUserSearch(HistorySearchDto historySearchDto) {
        //校验参数
        if(historySearchDto.getId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //是否登录
        ApUser apUser = AppThreadLocalUtil.getUser();
        if(apUser==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        mongoTemplate.remove(Query.query(Criteria.where("userId").is(apUser.getId()).and("id").is(historySearchDto.getId())),ApUserSearch.class);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
