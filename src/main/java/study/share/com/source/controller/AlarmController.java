package study.share.com.source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.service.AlarmService;

@Controller
public class AlarmController {

    @Autowired
    AlarmService alarmService;
    //피드 댓글 알림
    @MessageMapping("/reply")
    @SendTo("/alert/feedreply")
    public FeedReply alertreply(FeedReply feedReply,long id) throws Exception {
        System.out.println("Id: "+feedReply.getId());
        System.out.println("content: "+ feedReply.getContent());
        alarmService.alarmReply(feedReply, id);
        return feedReply;
    }

    //피드 좋아요 알림
    @MessageMapping("/like")
    @SendTo("/alert/feedlike")
    public FeedList alertlike(FeedList feedList, User user) throws Exception {
        System.out.println("Id: "+feedList.getId());
        System.out.println("user nickname:"+feedList.getUser().getNickname());
        System.out.println("content: "+ feedList.getContent());
        alarmService.alarmlike(feedList,user);
        return feedList;
    }
}
