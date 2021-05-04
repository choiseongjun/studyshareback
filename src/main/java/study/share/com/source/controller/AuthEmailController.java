package study.share.com.source.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import study.share.com.source.model.AccountType;
import study.share.com.source.model.User;
import study.share.com.source.service.UserService;
import study.share.com.source.service.VerificationTokenService;

import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/email")
public class AuthEmailController {

    @Autowired
    VerificationTokenService verificationTokenService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/send")
    public ResponseEntity<String> mailSending(
            @RequestParam("user_no") Long userNo,
            @RequestParam("email") String email) throws AddressException, MessagingException {

        verificationTokenService.sendAuthEmail(userNo, email);

        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "/verify")
    public ResponseEntity<String> verified(@RequestParam("token") String token) {

        verificationTokenService.verify(AccountType.email, token);
        return ResponseEntity.ok("success");
    }

    @ApiOperation(value="아이디 찾기",notes="아이디 찾기")
    @GetMapping(value = "/find/userId/{email}")
    public ResponseEntity<String> UserIdemailSending(
            @PathVariable("email") String email) throws AddressException, MessagingException {
        try{
            Optional<User> result=userService.findByEmail(email);
            result.orElseThrow(()->new NoSuchElementException("해당 이메일을 가진 사용자가 존재하지 않습니다"));
            verificationTokenService.userIdEmailsend(email,result.get());
            return ResponseEntity.ok("success");
        }
        catch(Exception e)
        {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
