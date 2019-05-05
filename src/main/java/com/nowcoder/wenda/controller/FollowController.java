package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.async.EventModel;
import com.nowcoder.wenda.async.EventProducer;
import com.nowcoder.wenda.async.EventType;
import com.nowcoder.wenda.model.Comment;
import com.nowcoder.wenda.model.EntityType;
import com.nowcoder.wenda.model.HostHolder;
import com.nowcoder.wenda.model.Question;
import com.nowcoder.wenda.service.*;
import com.nowcoder.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;


    @Autowired
    FollowService followService;

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId") int userId) {
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret? 0 :1 ,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId") int userId) {
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret? 0 :1 ,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        Question question = questionService.selectById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }

        boolean ret = followService.follow(hostHolder.getUser().getId(),
                EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwnerId(question.getUserId()));

        Map<String,Object> info = new HashMap<>();
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("name",hostHolder.getUser().getName());
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));

        return WendaUtil.getJSONString(ret? 0 :1 ,info);
    }

    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if(hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        Question question = questionService.selectById(questionId);
        if(question==null){
            return WendaUtil.getJSONString(1,"问题不存在");
        }

        boolean ret = followService.unfollow(hostHolder.getUser().getId(),
                EntityType.ENTITY_QUESTION,questionId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwnerId(question.getUserId()));

        Map<String,Object> info = new HashMap<>();
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION,questionId));

        return WendaUtil.getJSONString(ret? 0 :1 ,info);
    }

//    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
//    @ResponseBody
//    public String followQuestion(Model model,
//                                 @PathVariable("uid") int userId) {
//
//    }

}
