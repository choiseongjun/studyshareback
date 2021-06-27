package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.DTO.FeedListDTO;
import study.share.com.source.model.DTO.FeedListLikeDTO;
import study.share.com.source.model.DTO.StudyFeedListDTO;
import study.share.com.source.model.DTO.StudyFeedListLikeDTO;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedLike;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.studygroup.StudyFeedLike;
import study.share.com.source.model.studygroup.StudyFeedList;
import study.share.com.source.model.studygroup.StudyGroup;
import study.share.com.source.model.studygroup.StudyGroupMember;
import study.share.com.source.repository.ReportFeedRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.repository.feed.FeedListRepository;
import study.share.com.source.repository.study.StudyFeedListRepository;
import study.share.com.source.service.*;
import study.share.com.source.utils.HashTagExtract;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StudyFeedListController {

    @Autowired
    UserService userService;
    @Autowired
    StudyFeedListService studyFeedListService;
    @Autowired
    StudyFeedListRepository studyFeedListRepository;
    @Autowired
    HashTagExtract hashTagExtract;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StudyGroupService studyGroupService;
    @Autowired
    StudyMemberService studyGroupMemberService;

    //추가구현 수정
    @Autowired
    AlarmController alarmController;


    @ApiOperation(value="스터디 피드리스트 작성",notes="스터디 피드리스트 작성")
    @PostMapping("/study/feed/{groupid}")
    public ResponseEntity<?> savefeed(@RequestParam(name = "images", required = false) String file
            , @RequestParam(name = "content", required = false) String content, @PathVariable long groupid, Principal principal) throws IOException {
        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            Optional <StudyGroup> studyGroup = studyGroupService.findgroup(groupid);
            Optional <StudyGroupMember> studyGroupMember = studyGroupMemberService.findgroupmember(studyGroup.get(),user.get());

            if(!studyGroup .isPresent())
                return new ResponseEntity<>("해당 그룹이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            if(!studyGroupMember.isPresent())
                return new ResponseEntity<>("해당 그룹의 멤버가 아닙니다", HttpStatus.BAD_REQUEST);
            String eraseTag=studyFeedListService.remakeTag(content);
            StudyFeedList feedlist=studyFeedListService.saveFeed(user.get(),eraseTag,file,studyGroup.get());
            if(eraseTag!=null)
                studyFeedListService.extractHashTag(content,feedlist);//해시태그 검출 및 저장-> 테이블 사용시 다시
            return new ResponseEntity<>(new StudyFeedListDTO(feedlist), HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value="스터디 피드리스트 조회",notes="스터디 피드리스트 조회")
    @GetMapping("/study/feed/{groupid}")
    public ResponseEntity<?> listfeed(@PathVariable long groupid,Pageable pageable){
		try {
            Optional <StudyGroup> studyGroup = studyGroupService.findgroup(groupid);
            if(studyGroup.isPresent()) {
                int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
                pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
                Page<StudyFeedList> feedlist = studyFeedListService.listfeed(pageable);
                return new ResponseEntity<>(feedlist.stream().map(StudyFeedListDTO::new),HttpStatus.OK);
            }else {
                return new ResponseEntity<>("해당 그룹이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
            }
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
    }

    @ApiOperation(value="스터디 피드리스트 상세조회",notes="스터디 피드리스트 상세조회")
    @GetMapping("/study/feedDetail/{id}")
    public ResponseEntity<?> listfeedDetail(@PathVariable long id,Principal principal){
        try{
            if(principal==null) {
                Optional<StudyFeedList> feedlist = studyFeedListService.listfeedDetail(id);
                if(feedlist.isPresent())//피드가 존재하지 않는 경우
                    return new ResponseEntity<>("해당 사용자의 피드가 존재하지 않습니다",HttpStatus.BAD_REQUEST);

                return new ResponseEntity<>(new StudyFeedListDTO(feedlist.get()),HttpStatus.OK);
            }else {
                Optional<User> user = userService.findUserNickname(principal.getName());
                Optional<StudyFeedList> feedlist = studyFeedListService.listMyFeedLikeFeedDetail(id,user);
                if(!user.isPresent())
                    return new ResponseEntity<>("해당 사용자가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
                if(!feedlist.isPresent())
                    return new ResponseEntity<>("해당 사용자의 피드가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new StudyFeedListDTO(feedlist.get(),user.get()),HttpStatus.OK);
            }
        }catch(Exception e) {
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value="피드 수정",notes="피드 수정")
    @PatchMapping("/study/feed/{id}")
    public ResponseEntity<?> updatefeed(
            @RequestParam(name = "images", required = false) String file,
            @PathVariable long id,
            @RequestParam(name = "content", required = false) String content,
            Principal principal){
        try {
//			String content = data.get("content");
            Optional<User> user = userService.findUserNickname(principal.getName());
            Optional<StudyFeedList> feedlist = studyFeedListService.updatefeed(id,content,file);
            return new ResponseEntity<>(feedlist,HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="스터디 피드 삭제",notes="스터디 피드 삭제")
    @DeleteMapping("/study/feed/{id}")
    public ResponseEntity<?> deletefeed(@PathVariable long id){
        try {
            studyFeedListService.deletefeed(id);
            return new ResponseEntity<>(id,HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    /*좋아요 */
    @ApiOperation(value="스터디 피드 좋아요",notes="스터디 피드 좋아요")
//    @SendTo("/alert/feedlike")
    @PostMapping("/study/likefeed/{id}")
    public ResponseEntity<?> likefeed(@PathVariable long id,Principal principal){
        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            Optional<StudyFeedList> checkFeedLikeUser = studyFeedListRepository.findByIdAndStudyfeedlikeUserId(id,user.get().getId());
            if(checkFeedLikeUser.isPresent()) {
                return new ResponseEntity<>("이미 좋아요를 눌렀습니다.",HttpStatus.BAD_REQUEST);
            }
            Optional<StudyFeedList> feedList = studyFeedListService.likefeed(user,id);
            StudyFeedListLikeDTO feedListLike=new StudyFeedListLikeDTO(feedList.get(),user.get());
            feedListLike.setUserKey(user.get().getId());

      //      alarmController.alertlike(feedList.get(),user.get());//피드 좋아요 사용자에게 알림

            return new ResponseEntity<>(feedListLike,HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="스터디 피드 좋아요 취소",notes="스터디 피드 좋아요 취소")
    @DeleteMapping("/study/likefeed/{id}")
    public ResponseEntity<?> dislikefeed(@PathVariable long id,Principal principal){
        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            Optional<StudyFeedList> feedList = studyFeedListService.dislikefeed(user,id);
            StudyFeedListLikeDTO feedListLike=new StudyFeedListLikeDTO(feedList.get(),user.get());
            feedListLike.setUserKey(user.get().getId());
            return new ResponseEntity<>(feedListLike,HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="스터디 게시글별 좋아요리스트 조회",notes="스터디 게시글별 좋아요리스트 조회")
    @GetMapping("/study/feed/likefeedlist/{id}")
    public ResponseEntity<?> likefeedlist(@PathVariable long id,Principal principal){
        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            List<StudyFeedLike> feedlike=studyFeedListService.selectFeedlikelist(id);
            for(StudyFeedLike feedlikefollow : feedlike) {
                for(Follow f:feedlikefollow.getUser().getFollow()) {
                    if(f.getFromUser().getId()==user.get().getId()) {
                        //좋아요 유저의 팔로우정보를 들고와서 from으로부터 한 아이디와 내정보아이디를 비교한다 맞으면 true
                        feedlikefollow.setTempFollow(true);//임시변수로 팔로우한지 안한지 처리
                    }
                }
            }
            return new ResponseEntity<Object>(feedlike,HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="스터디 내 피드 조회",notes="스터디 내 피드 조회")
    @GetMapping("/study/feed/my")
    public ResponseEntity<?> mylistfeed(Pageable pageable,Principal principal){
        try {
            Optional<User> user = userService.findUserNickname(principal.getName());
            int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
            pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
            Page<StudyFeedList> feedlist = studyFeedListService.mylistfeed(pageable,user.get().getId());
            return new ResponseEntity<>(feedlist.stream().map(StudyFeedListDTO::new),HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="다른사람 피드 조회",notes="다른사람 피드 조회")
    @GetMapping("/study/feed/other/{id}")
    public ResponseEntity<?> otherlistfeed(Pageable pageable,@PathVariable long id){

        try {
            int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
            pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
            Page<StudyFeedList> feedlist = studyFeedListService.otherlistfeed(pageable,id);
            return new ResponseEntity<>(feedlist.stream().map(StudyFeedListDTO::new),HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

}
