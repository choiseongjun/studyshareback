package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.share.com.source.message.request.StudyGroupReq;
import study.share.com.source.model.StudyGroup;
import study.share.com.source.model.User;
import study.share.com.source.service.StudyGroupService;
import study.share.com.source.service.StudyMemberService;
import study.share.com.source.service.UserService;

import java.io.IOException;
import java.security.Principal;
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
    public ResponseEntity<?> savemember(@PathVariable long groupid, Principal principal) throws IOException {
        try {
            Optional<User> user= userService.findUserNickname(principal.getName());
            Optional <StudyGroup>studygroup = studyGroupService.findgroup(groupid);
            if(studygroup.get().getOwner().getId()!=user.get().getId())//그룹장이 아닌 경우
            {
                studyMemberService.savemember(user.get(),studygroup.get(),'n');
            }
            return new ResponseEntity<>("그룹이 생성되었습니다", HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }


}
