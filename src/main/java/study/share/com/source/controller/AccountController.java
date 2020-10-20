package study.share.com.source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.share.com.source.model.AccountType;
import study.share.com.source.model.DTO.AuthTokenDTO;
import study.share.com.source.model.User;
import study.share.com.source.model.exception.GeneralErrorException;
import study.share.com.source.service.ExternalAccountService;
import study.share.com.source.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    ExternalAccountService externalAccountService;

    @Autowired
    UserService userService;

    @PostMapping("/connect_by_external")
    public ResponseEntity<?> connectByExternal(
            @RequestParam("account_type") AccountType accountType,
            @RequestParam("token") String token, Principal principal) {

        User user = userService.findUserNickname(principal.getName()).orElseThrow(() -> new GeneralErrorException());

        externalAccountService.connect(user, accountType, token);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/disconnect_by_external")
    public ResponseEntity<?> disconnectByExternal(
            @RequestParam("account_type") AccountType accountType, Principal principal) {

        User user = userService.findUserNickname(principal.getName()).orElseThrow(() -> new GeneralErrorException());

        externalAccountService.disconnect(user, accountType);
        return ResponseEntity.ok("success");
    }
}
