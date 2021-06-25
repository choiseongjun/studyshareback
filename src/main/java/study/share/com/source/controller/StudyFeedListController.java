package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.share.com.source.model.DTO.FeedListDTO;
import study.share.com.source.model.DTO.StudyFeedListDTO;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedList;
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

    @Autowired
    BlockedUserService blockedUserService;

//    @ApiOperation(value="스터디 피드리스트 작성",notes="스터디 피드리스트 작성")
//    @PostMapping("/study/{groupid}/feed")
//    public ResponseEntity<?> savefeed(@RequestParam(name = "images", required = false) String file
//            , @RequestParam(name = "content", required = false) String content, @PathVariable long groupid, Principal principal) throws IOException {
//        try {
//            Optional<User> user = userService.findUserNickname(principal.getName());
//            Optional <StudyGroup> studyGroup = studyGroupService.findgroup(groupid);
//            Optional <StudyGroupMember> studyGroupMember =
//            String eraseTag=studyFeedListService.remakeTag(content);
//            StudyFeedList feedlist=studyFeedListService.saveFeed(user.get(),eraseTag,file,studyGroup.get());
//            if(eraseTag!=null)
//                studyFeedListService.extractHashTag(content,feedlist);//해시태그 검출 및 저장-> 테이블 사용시 다시
//
//            return new ResponseEntity<>(new StudyFeedListDTO(feedlist), HttpStatus.OK);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
//        }
//
//    }

}
