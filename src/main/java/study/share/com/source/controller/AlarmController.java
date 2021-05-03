package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import study.share.com.source.message.response.UserProfileResponse;
import study.share.com.source.message.response.UserResponse;
import study.share.com.source.model.*;
import study.share.com.source.model.DTO.AlarmHistoryDTO;
import study.share.com.source.service.AlarmService;
import study.share.com.source.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AlarmController {

    @Autowired
    AlarmService alarmService;
    @Autowired
    UserService userService;
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

    //유저의 전체 알람 조회
    @ApiOperation(value="사용자의 알람 조회하기",notes="사용자의 알람 조회하기")
    @MessageMapping("/alarmView")
    @SendTo("/alert/view")
    //@GetMapping("/message/alarm")
    public ResponseEntity<?> allalertView(Principal principal) throws Exception {

        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            List<AlarmHistory> alarmHistoryList=alarmService.allalarmView(user.get());
            if(alarmHistoryList.isEmpty())//조회한 알람이 없는 경우
                return ResponseEntity.ok("알람이 없습니다");

            List<AlarmHistoryDTO> alarmHistoryDTOList = new ArrayList<>();
            alarmHistoryList.stream()
                    .filter(alarmHistory -> alarmHistory!=null)
                    .forEach( alarmHistory -> {
                        alarmHistoryDTOList.add(new AlarmHistoryDTO(alarmHistory));
             });
            return new ResponseEntity<>(alarmHistoryDTOList,HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
        }
    }
}
