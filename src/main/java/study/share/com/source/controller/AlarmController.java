package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import study.share.com.source.model.*;
import study.share.com.source.model.DTO.AlarmHistoryDTO;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.repository.feed.FeedListRepository;
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
    @Autowired
    private SimpMessagingTemplate webSocket;
    @Autowired
    FeedListRepository feedListRepository;


    //피드 댓글 알림
    @MessageMapping("/reply")
    @SendTo("/noti/feedlike")
    public FeedReply alertreply(FeedReply feedReply, long id) throws Exception {
        alarmService.alarmReply(feedReply, id);
        webSocket.convertAndSend("/noti/feedlike/"+feedReply.getUser().getId(), feedReply);
        return feedReply;
    }

    //피드 좋아요 알림
    @MessageMapping("/like")
    @SendTo("/noti/feedlike")
    public FeedList alertlike(FeedList feedList, User user) throws Exception {
        alarmService.alarmlike(feedList,user);
        webSocket.convertAndSend("/noti/feedlike/"+feedList.getUser().getId(), feedList);
        return feedList;
    }

    //유저의 전체 알람 조회
    //@ApiOperation(value="사용자의 알람 조회하기",notes="사용자의 알람 조회하기")
    @MessageMapping("/alarmView")
    @SendTo("/noti/view")
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
            webSocket.convertAndSend("/noti/view/"+user.get().getId(), alarmHistoryDTOList);
            return new ResponseEntity<>(alarmHistoryDTOList,HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
        }
    }

    //테스트용 코드
   @MessageMapping("/likes")
    @SendTo("/noti/feedlikes")
    public FeedList alertlikes(String id) throws Exception {

        //webSocket.convertAndSend("/noti/feedlike/"+feedList.getUser().getId(), feedList);
        System.out.println(id);
        Optional <FeedList> result= feedListRepository.findById(Long.valueOf(80));
        System.out.println(result.get().getContent());
        return result.get();
    }

    @MessageMapping("/count")
    @SendTo("/noti/count")
    public long alertCount(Principal principal) throws Exception {
        Optional <User> user = userService.findUserNickname(principal.getName());
        long alarmCount = alarmService.alarmCount(user.get().getId());
        System.out.println("result: "+alarmCount);
        webSocket.convertAndSend("/noti/count"+user.get().getId(), alarmCount);
        return alarmCount;
    }


    //1이면 허용 0이면 거부
    @ApiOperation(value="알림 수신 허용 여부",notes="알림 수신 허용")
    @PostMapping("/noti/send/{check}")
    public ResponseEntity<?> alarmChecked(Principal principal,@PathVariable boolean check) {
        Optional <User> user = userService.findUserNickname(principal.getName());
        userService.saveAlarmCheck(user.get(),check);
        return new ResponseEntity<String>("알람 설정이 변경 되었습니다.", HttpStatus.OK);
    }

}
