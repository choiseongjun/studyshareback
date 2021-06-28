package study.share.com.source.controller.study;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import study.share.com.source.message.request.StudyGroupReq;
import study.share.com.source.model.DTO.study.StudyGroupDTO;
import study.share.com.source.model.study.StudyGroup;
import study.share.com.source.model.User;
import study.share.com.source.service.study.StudyGroupService;
import study.share.com.source.service.study.StudyMemberService;
import study.share.com.source.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class StudyGroupController {

    @Autowired
    UserService userService;
    @Autowired
    StudyGroupService groupService;
    @Autowired
    StudyMemberService studyMemberService;

    @ApiOperation(value="그룹 생성",notes="그룹 생성")
    @PostMapping("/group")
    public ResponseEntity<?> savegroup(@RequestBody StudyGroupReq groupReq, Principal principal) throws IOException {
        try {
            Optional<User> Owner= userService.findUserNickname(principal.getName());
            StudyGroup group = groupService.savegroup(groupReq,Owner.get());
            studyMemberService.savemember(Owner.get(),group,'n');//그룹장 그룹의 회원으로 가입
            return new ResponseEntity<>("그룹이 생성되었습니다", HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value="그룹 조회",notes="그룹 조회")
    @GetMapping("/group/{groupid}")
    public ResponseEntity<?> findgroup(@PathVariable long groupid) throws IOException {
            Optional<StudyGroup> group = groupService.findgroup(groupid);
            if(group.isPresent())
                return new ResponseEntity<>(new StudyGroupDTO(group.get()), HttpStatus.OK);
            else
                return new ResponseEntity<>("해당 그룹이 존재하지 않습니다", HttpStatus.OK);
    }


    ///외래키 때문에 삭제불가 수정 필요 -> 지금 안됨
    @ApiOperation(value="그룹 삭제",notes="그룹 삭제")
    @DeleteMapping("/group/{groupid}")
    public ResponseEntity<?> deletegroup(@PathVariable long groupid,Principal principal) throws IOException {
        Optional<StudyGroup> group = groupService.findgroup(groupid);
        Optional <User> Owner= userService.findUserNickname(principal.getName());
        if(!group.isPresent())
            return new ResponseEntity<>("해당 그룹이 존재하지 않습니다", HttpStatus.OK);
        if(group.get().getOwner().getId()==Owner.get().getId())//그룹장인 경우에만 삭제
        {
            groupService.deletegroup(groupid);
            return new ResponseEntity<>("그룹 "+groupid+"번이 삭제 되었습니다", HttpStatus.OK);
        }
        return new ResponseEntity<>("그룹을 삭제할 수 있는 권한이 없습니다", HttpStatus.OK);
    }

    @ApiOperation(value="그룹 정보 수정",notes="그룹 정보 수정")
    @PatchMapping("/group/{groupid}")
    public ResponseEntity<?> modifygroup(@RequestBody StudyGroupReq groupReq,@PathVariable long groupid,Principal principal) throws IOException {
        Optional<StudyGroup> group = groupService.findgroup(groupid);
        Optional <User> Owner= userService.findUserNickname(principal.getName());
        if(!group.isPresent())
            return new ResponseEntity<>("해당 그룹이 존재하지 않습니다", HttpStatus.OK);
        if(group.get().getOwner().getId()==Owner.get().getId())//그룹장인 경우에만 삭제
        {
            StudyGroup studyGroup=groupService.modifygroup(groupid,groupReq);
            return new ResponseEntity<>(new StudyGroupDTO(studyGroup), HttpStatus.OK);
        }
        return new ResponseEntity<>("그룹을 삭제할 수 있는 권한이 없습니다", HttpStatus.OK);
    }

    @ApiOperation(value="그룹 전체 조회",notes="그룹 전체 조회")
    @GetMapping("/group")
    public ResponseEntity<?> findAllgroup() throws IOException {
        List<StudyGroup> grouplist = groupService.findAllgroup();
        if(!grouplist.isEmpty())
            return new ResponseEntity<>(grouplist.stream().map(StudyGroupDTO::new), HttpStatus.OK);
        else
            return new ResponseEntity<>("그룹이 존재하지 않습니다", HttpStatus.OK);
    }
}
