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
    @PostMapping("/study/{groupid}/feed")
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
    @GetMapping("/study/{groupid}/feed")
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

//    @ApiOperation(value="스터디 피드리스트 상세조회",notes="스터디 피드리스트 상세조회")
//    @GetMapping("/study/feedDetail/{id}")
//    public ResponseEntity<?> listfeedDetail(@PathVariable long id,Principal principal){
//        if(principal==null) {
//             StudyFeedList feedlist = studyFeedListService.listfeedDetail(id);
//            return new ResponseEntity<>(new StudyFeedListDTO(feedlist),HttpStatus.OK);
//
//        }else {
//            Optional<User> user = userService.findUserNickname(principal.getName());
//            Optional<StudyFeedList> feedlist = studyFeedListService.listMyFeedLikeFeedDetail(id,user);
//            if(!user.isPresent())
//                return new ResponseEntity<>("해당 사용자가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
//            if(!feedlist.isPresent())
//                return new ResponseEntity<>("해당 사용자의 피드가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<>(new StudyFeedListDTO(feedlist.get(),user.get()),HttpStatus.OK);
//        }
//
//    }

}
