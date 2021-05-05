package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.share.com.source.message.request.PasswordChangeReq;
import study.share.com.source.model.User;
import study.share.com.source.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Controller
public class ViewController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder encoder;

    @ApiOperation(value="비밀번호 변경 페이지 실행",notes="비밀번호 변경 페이지 실행")
    @RequestMapping(value="PwFind/{email}", method = RequestMethod.GET)
    public String changePWpage(Model model,@PathVariable("email")String email) throws IOException {
        Optional<User> user=userService.findByEmail(email);
        model.addAttribute("user",user.get());
        return "PwFind";
    }

    @ApiOperation(value="비밀번호 변경 값 반환",notes="비밀번호 변경 값 반환")
    @PostMapping("/change/password/set/")
    @ResponseBody
    public ResponseEntity<?> changepassword(@RequestParam(required = false) String userId
            ,@RequestParam(required = false) String password)
            throws IOException {
        try {
            PasswordChangeReq passwordChangeReq = new PasswordChangeReq();
            passwordChangeReq.setNewPassword(password);
            passwordChangeReq.setUserId(userId);
            Optional<User> result=userService.checkpasswordSet(passwordChangeReq);
            return new ResponseEntity<>("비밀번호 변경 성공", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
