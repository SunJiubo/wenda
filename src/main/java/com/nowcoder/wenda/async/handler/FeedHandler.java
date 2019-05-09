package com.nowcoder.wenda.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.wenda.async.EventHandler;
import com.nowcoder.wenda.async.EventModel;
import com.nowcoder.wenda.async.EventType;
import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.Feed;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.model.User;
import com.nowcoder.wenda.service.FeedService;
import com.nowcoder.wenda.service.FollowService;
import com.nowcoder.wenda.service.QuestionService;
import com.nowcoder.wenda.service.UserService;
import com.nowcoder.wenda.util.JedisAdapter;
import com.nowcoder.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    private String buildFeedData(EventModel model){
        System.out.println("进来了");
        Map<String, String> map = new HashMap<>();
        //触犯用户是通用的
        User actor = userService.getUser(model.getActorId());
        if(actor==null){
            return null;
        }

        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());

        if(model.getType()==EventType.COMMENT||
                (model.getType()==EventType.FOLLOW&&
                        model.getEntityType() == EntityType.ENTITY_QUESTION)){
            Question question = questionService.selectById(model.getEntityId());
            if(question==null)
                return null;
            map.put("question",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        Random r = new Random();
//        model.setActorId(1+r.nextInt(10));

        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));
        if(feed.getData()==null){
            //不支持的Feed
            return;
        }
        feedService.addFeed(feed);

        //获取所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);

        //系统队列
        followers.add(0);
        //给所有的粉丝推世间
        for (int follower :
                followers) {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
            //限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.FOLLOW,EventType.COMMENT});
    }
}
