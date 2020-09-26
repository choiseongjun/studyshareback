package study.share.com.source.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.Follow;
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
	/*내 팔로우정보 불러오기 2020-09-26 choiseongjun */
	@GetMapping("/user/followlist")
	public ResponseEntity<?> followlist(Principal principal){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			List<Follow> followlist=userService.followlist(user);
			return new ResponseEntity<>(followlist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
		
		
	}
	/*id는 팔로워*/
	@PostMapping("/user/following/{id}")
	public ResponseEntity<?> following(@PathVariable long id,Principal principal){
	
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			userService.following(id,user);
		}catch(Exception e) {
			
		}
		
		return null;
	}
	@DeleteMapping("/user/following/{id}")
	public ResponseEntity<?> canclefollowing(@PathVariable long id,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			userService.canclefollowing(id,user);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
