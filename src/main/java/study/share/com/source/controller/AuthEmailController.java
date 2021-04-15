package study.share.com.source.controller;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.AccountType;
import study.share.com.source.service.VerificationTokenService;


@RestController
@RequestMapping("/email")
public class AuthEmailController {

    @Autowired
    VerificationTokenService verificationTokenService;

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

    @RequestMapping(value = "/send/pw/change")
    public ResponseEntity<String> pwchangemailSending(
            @RequestParam("email") String email) throws AddressException, MessagingException {
        verificationTokenService.pwchangeEmailsend(email);
        return ResponseEntity.ok("success");
    }
}
