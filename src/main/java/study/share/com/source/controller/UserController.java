package study.share.com.source.controller;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.User;
import study.share.com.source.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/api/auth/userinfo")
	public ResponseEntity<?> userAccess(Principal principal) {
		
		try {			
			Optional<User> user = userService.findUserNickname(principal.getName());
			return ResponseEntity.ok(user.get());
		}catch(Exception e) {
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
}
