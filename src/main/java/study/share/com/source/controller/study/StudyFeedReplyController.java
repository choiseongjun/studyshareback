package study.share.com.source.controller.study;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.share.com.source.controller.AlarmController;
import study.share.com.source.model.DTO.feed.FeedReplyDTO;
import study.share.com.source.model.DTO.feed.FeedReplyLikeDTO;
import study.share.com.source.model.DTO.study.StudyFeedReplyDTO;
import study.share.com.source.model.DTO.study.StudyFeedReplyLikeDTO;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.study.StudyFeedList;
import study.share.com.source.model.study.StudyFeedReply;
import study.share.com.source.model.study.StudyFeedReplyLike;
import study.share.com.source.repository.study.StudyFeedReplyLikeRepository;
import study.share.com.source.repository.study.StudyFeedReplyRepository;
import study.share.com.source.service.UserService;
import study.share.com.source.service.study.StudyFeedListService;
import study.share.com.source.service.study.StudyFeedReplyService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class StudyFeedReplyController {

    @Autowired
    StudyFeedReplyService studyFeedReplyService;
    @Autowired
    UserService userService;
    @Autowired
    StudyFeedReplyLikeRepository studyFeedReplyLikeRepository;
    @Autowired
    AlarmController alarmController;
    @Autowired
    StudyFeedListService studyFeedListService;

    public static List<StudyFeedReply> replyContnet = new ArrayList<StudyFeedReply>();//대댓글 쓸때 필요함 2021 04-18 choiseongjun
    public static List<StudyFeedReply> feedReplyUser = new ArrayList<StudyFeedReply>();//댓글조회해서 내가 좋아요누른거 가져오기

    @ApiOperation(value="스터디 피드 게시글별 댓글 작성",notes="스터디 피드 게시글별 댓글 작성")
    @PostMapping("/study/feed/reply/{id}")
    public ResponseEntity<?> addfeedcomment(@PathVariable long id, Principal principal, @RequestBody Map<String, String> data){
        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            String content = data.get("content");
            Optional <StudyFeedList > findFeed = studyFeedListService.listfeedDetail(id);
            if(!findFeed.isPresent())
                return new ResponseEntity<>("해당 피드가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
            Optional<StudyFeedReply>feedReplylist=studyFeedReplyService.addfeedcomment(id,user,content);

            //alarmController.alertreply(feedReplylist, id);//댓글 생성 알림

            return new ResponseEntity<>(new StudyFeedReplyDTO(feedReplylist.get()), HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="스터디 게시글별 댓글 수정",notes="스터디 게시글별 댓글 수정")
    @PatchMapping("/study/feed/reply/{id}")
    public ResponseEntity<?> updatefeedcomment(@PathVariable long id,Principal principal,@RequestBody Map<String, String> data){
        try {
            if(principal==null) {
                return new ResponseEntity<>("로그인을 해주세요",HttpStatus.BAD_REQUEST);
            }
            Optional<User> user = userService.findUserNickname(principal.getName());
            Optional<StudyFeedReply> studyFeedReply = studyFeedReplyService.checkusercomment(id);//댓글작성자랑 로그인한 유저가 같은지 비교
            if(!studyFeedReply.isPresent())
                return new ResponseEntity<>("해당 댓글을 찾을 수 없습니다",HttpStatus.BAD_REQUEST);
            if(user.get().getId()!=studyFeedReply.get().getUser().getId()) {
                return new ResponseEntity<>("작성자가 다릅니다",HttpStatus.BAD_REQUEST);
            }else {
                String content = data.get("content");
                StudyFeedReply feedReplylist=studyFeedReplyService.updatefeedcomment(id,user,content);

                return new ResponseEntity<>(new StudyFeedReplyDTO(feedReplylist),HttpStatus.OK);
            }
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="스터디 게시글별 댓글 삭제",notes="스터디 게시글별 댓글 삭제")
    @DeleteMapping("/study/feed/reply/{id}")
    public ResponseEntity<?> removefeedcomment(@PathVariable("id") long id,Principal principal){
        try {
            if(principal==null) {
                return new ResponseEntity<>("로그인을 해주세요",HttpStatus.BAD_REQUEST);
            }

            Optional<User> user = userService.findUserNickname(principal.getName());
            Optional<StudyFeedReply> studyFeedReply = studyFeedReplyService.checkusercomment(id);//댓글작성자랑 로그인한 유저가 같은지 비교
            if(!studyFeedReply.isPresent())
                return new ResponseEntity<>("해당 댓글을 찾을 수 없습니다",HttpStatus.BAD_REQUEST);
            if(user.get().getId()!=studyFeedReply.get().getUser().getId()) {
                return new ResponseEntity<>("작성자가 다릅니다",HttpStatus.BAD_REQUEST);
            }else {
                studyFeedReplyService.removefeedcomment(id,user);
                long postId = studyFeedReplyService.findPostId(id);
                Map<String, Long> data=new HashMap<String, Long>();
                data.put("commentId", id);
                data.put("postId",postId);
                return new ResponseEntity<>(data,HttpStatus.OK);
            }

        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="게시글별 댓글 조회",notes="게시글별 댓글 조회")
    @GetMapping("/study/feed/reply/{id}")
    public ResponseEntity<?> getfeedreply(@PathVariable long id, Pageable pageable, Principal principal){

        if(principal==null) {
            Page<StudyFeedReply> feedReplylist=studyFeedReplyService.getfeedreply(id,pageable);
            List<Long> feedreplyId = feedReplylist.stream().map(t->t.getId()).collect(Collectors.toList());

            for(int i=0;i<feedreplyId.size();i++) {
                Page<StudyFeedReply> feedReReplylist=studyFeedReplyService.getfeedrereply(feedreplyId.get(i),pageable);
                replyContnet= feedReReplylist.getContent();
            }
            return new ResponseEntity<>(feedReplylist.stream().map(t->new StudyFeedReplyDTO(t)),HttpStatus.OK);

        }else {
            Optional<User> user = userService.findUserNickname(principal.getName());
            Page<StudyFeedReply> feedReplylist=studyFeedReplyService.getfeedreply(id,pageable);
            List<Long> feedreplyId = feedReplylist.stream().map(t->t.getId()).collect(Collectors.toList());

            for(int i=0;i<feedreplyId.size();i++) {
                Page<StudyFeedReply> feedReReplylist=studyFeedReplyService.getfeedrereply(feedreplyId.get(i),pageable);
                replyContnet= feedReReplylist.getContent();
            }
            return new ResponseEntity<>(feedReplylist.stream().map(t->new StudyFeedReplyDTO(t,user.get())),HttpStatus.OK);

        }
    }

//    @ApiOperation(value="스터디 댓글 좋아요",notes="스터디 댓글 좋아요")
//    @PostMapping("/study/likefeedreply/{id}")
//    public ResponseEntity<?> likefeedreply(@PathVariable long id,Principal principal){
//        try {
//            Optional<User> user = userService.findUserNickname(principal.getName());
//            Optional<StudyFeedReplyLike> userResult= studyFeedReplyLikeRepository.findByStudyFeedReplyIdAndUserId(id,user.get().getId());
//            if(userResult.isPresent()) {
//                return new ResponseEntity<>("이미 좋아요를 눌렀습니다.",HttpStatus.BAD_REQUEST);
//            }
//            StudyFeedReplyLikeDTO feedReplylike = studyFeedReplyService.likefeedreply(user,id);
//
//            return new ResponseEntity<>(feedReplylike,HttpStatus.OK);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
//        }
//    }
//    @ApiOperation(value="댓글 좋아요 취소",notes="댓글 좋아요 취소 ")
//    @DeleteMapping("/study/likefeedreply/{id}")
//    public ResponseEntity<?> likefeedreplyCancel(@PathVariable long id,Principal principal){
//        try {
//            Optional<User> user = userService.findUserNickname(principal.getName());
//            studyFeedReplyService.likeCanclefeedreply(user,id);
//            FeedReplyLikeDTO feedReplylike = new FeedReplyLikeDTO();
//
//            return new ResponseEntity<>(feedReplylike,HttpStatus.OK);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @ApiOperation(value="댓글의 댓글 작성",notes="댓글의 댓글 작성")
//    @PostMapping("/study/feed/reply/re/{feedid}/{id}")
//    public ResponseEntity<?> addfeedcommentreply(@PathVariable long feedid,@PathVariable long id,Principal principal,@RequestBody Map<String, String> data){
//        Optional<User> user = userService.findUserNickname(principal.getName());
//        String content = data.get("content");
//        StudyFeedReply feedReplylist=studyFeedReplyService.addfeedcommentReply(feedid,id,user,content);
//        StudyFeedReplyDTO feedReplyDTO=new StudyFeedReplyDTO(feedReplylist);
//        try {
//
//            return new ResponseEntity<>(feedReplyDTO,HttpStatus.OK);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
//        }
//    }


}
