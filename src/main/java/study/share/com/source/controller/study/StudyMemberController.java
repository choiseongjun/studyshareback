package study.share.com.source.controller.study;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import study.share.com.source.model.DTO.study.StudyGroupMemberDTO;
import study.share.com.source.model.study.StudyGroup;
import study.share.com.source.model.study.StudyGroupMember;
import study.share.com.source.model.User;
import study.share.com.source.service.study.StudyGroupService;
import study.share.com.source.service.study.StudyMemberService;
import study.share.com.source.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class StudyMemberController {

    @Autowired
    UserService userService;
    @Autowired
    StudyMemberService studyMemberService;
    @Autowired
    StudyGroupService studyGroupService;


    @ApiOperation(value="스터디 그룹 멤버 가입",notes="스터디 그룹 멤버 가입")
    @PostMapping("/group/{groupid}/member")
    public ResponseEntity<?> savemember(@PathVariable ("groupid")long groupid, Principal principal) throws IOException {
        try {
            Optional<User> user= userService.findUserNickname(principal.getName());
            Optional <StudyGroup>studygroup = studyGroupService.findgroup(groupid);

            Optional <StudyGroupMember> member = studyMemberService.findgroupmember(studygroup.get(),user.get());
            if(!member.isPresent())//가입하지 않은 경우에만 가입
            {
                if(studygroup.get().getOwner().getId()!=user.get().getId())//그룹장이 아닌 경우
                    studyMemberService.savemember(user.get(),studygroup.get(),'n');
                else
                    studyMemberService.savemember(user.get(),studygroup.get(),'y');
                return new ResponseEntity<>("스터디 멤버가 생성되었습니다", HttpStatus.OK);
            }
            return new ResponseEntity<>("스터디 멤버가 생성되지 않았습니다", HttpStatus.BAD_REQUEST);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="그룹 멤버 탈퇴",notes="그룹 멤버 삭제")
    @Transactional
    @DeleteMapping("/group/{groupid}/member")
    public ResponseEntity<?> deletegroupmember(@PathVariable ("groupid") long groupid,Principal principal) throws IOException {
        try{
            Optional<User> user= userService.findUserNickname(principal.getName());
            Optional<StudyGroup> group = studyGroupService.findgroup(groupid);
            Optional <StudyGroupMember> member = studyMemberService.findgroupmember(group.get(),user.get());
            if(member.isPresent())
            {
                studyMemberService.deletegroupmember(group.get(),user.get());
                return new ResponseEntity<>("그룹 탈퇴가 완료되었습니다", HttpStatus.OK);
            }
            return new ResponseEntity<>("그룹이 존재하지 않거나 회원이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="그룹 멤버 리스트 조회",notes="그룹 멤버 리스트 조회")
    @GetMapping("/group/{groupid}/member")
    public ResponseEntity<?> findAllgroupmember(@PathVariable ("groupid")long groupid) throws IOException {
        Optional<StudyGroup> group = studyGroupService.findgroup(groupid);
        List<StudyGroupMember> memberlist = studyMemberService.findAllgroupmember(group.get());
        if(!memberlist.isEmpty())
            return new ResponseEntity<>(memberlist.stream().map(StudyGroupMemberDTO::new), HttpStatus.OK);
        else
            return new ResponseEntity<>("해당 그룹 멤버들이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value="그룹 멤버 조회",notes="그룹 멤버 조회")
    @GetMapping("/group/{groupid}/{memberid}")
    public ResponseEntity<?> findgroupmember(@PathVariable ("groupid") long groupid,@PathVariable ("memberid")long memberid) throws IOException {
        try {
            Optional<StudyGroup> group = studyGroupService.findgroup(groupid);
            Optional<User> user = userService.findUserId(memberid);
            Optional <StudyGroupMember> member = studyMemberService.findgroupmember(group.get(),user.get());
            if(member.isPresent())
                return new ResponseEntity<>(new StudyGroupMemberDTO(member.get()), HttpStatus.OK);
            return new ResponseEntity<>("해당 그룹 멤버가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }


}
