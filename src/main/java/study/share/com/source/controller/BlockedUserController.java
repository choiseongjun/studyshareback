package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.DTO.AlarmHistoryDTO;
import study.share.com.source.model.DTO.BlockedUserDTO;
import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeed;
import study.share.com.source.service.BlockedUserService;
import study.share.com.source.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class BlockedUserController {

    @Autowired
    UserService userService;
    @Autowired
    BlockedUserService blockedUserService;

    @ApiOperation(value="사용자 차단하기",notes="사용자 차단하기")
    @PostMapping("/report/user/{id}")
    public ResponseEntity<?> blockUser(@PathVariable("id")long id, Principal principal){

            Optional<User> reporter = userService.findUserNickname(principal.getName());
            BlockedUser result=blockedUserService.blockedUserSave(reporter.get(),id);
            if(result.getId()!=-1)//신고 성공
            {
                BlockedUserDTO blockedUserDTO = new BlockedUserDTO(result);
                return new ResponseEntity<>( blockedUserDTO ,HttpStatus.OK);
            }
        return new ResponseEntity<>("사용자 신고 실패",HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value="사용자 차단 취소 하기",notes="사용자 차단 취소")
    @DeleteMapping("/report/user/{id}")
    public ResponseEntity<?> blockUserDelete(@PathVariable("id")long id, Principal principal){

        Optional<User> reporter = userService.findUserNickname(principal.getName());
        BlockedUser result=blockedUserService.blockedUserDelete(reporter.get(),id);
        if(result.getId()!=-1)
            return new ResponseEntity<>("사용자 신고 취소 성공",HttpStatus.OK);

        return new ResponseEntity<>("사용자 신고 취소 실패",HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value="사용자별 차단내역 조회",notes="사용자별 차단내역 조회")
    @GetMapping("/report/my")
    public ResponseEntity<?> blockedlistView(Pageable pageable, Principal principal){
        try {
            Optional<User> reporter = userService.findUserNickname(principal.getName());
            int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
            pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
            Page<BlockedUser> reportResult=blockedUserService.reportlist(pageable,reporter.get().getId());

            List <BlockedUserDTO> blockedUserDTOList= new ArrayList<>();
            reportResult.stream()
                    .filter(blockedUser -> blockedUser!=null)
                    .forEach( blockedUser -> {
                        blockedUserDTOList.add(new BlockedUserDTO(blockedUser));
                    });
            return new ResponseEntity<>( blockedUserDTOList, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }


}
