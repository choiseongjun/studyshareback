package study.share.com.source.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;

@Controller
public class MessageController {

    //피드 댓글 알림
    @MessageMapping("/reply")
    @SendTo("/alert/feedreply")
    public FeedReply alertreply(FeedReply feedReply) throws Exception {
        System.out.println("Id: "+feedReply.getId());
        System.out.println("content: "+ feedReply.getContent());
        return feedReply;
    }

    //피드 좋아요 알림
    @MessageMapping("/like")
    @SendTo("/alert/feedlike")
    public FeedList alertlike(FeedList feedList) throws Exception {
        System.out.println("Id: "+feedList.getId());
        System.out.println("user nickname:"+feedList.getUser().getNickname());
        System.out.println("content: "+ feedList.getContent());
        return feedList;
    }
}
