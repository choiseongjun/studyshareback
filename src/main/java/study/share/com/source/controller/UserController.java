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

import study.share.com.source.message.response.UserProfileResponse;
import study.share.com.source.message.response.UserResponse;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;
import study.share.com.source.service.FeedListService;
import study.share.com.source.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	FeedListService feedListService;
	
	@GetMapping("/api/auth/userinfo")
	public ResponseEntity<?> userAccess(Principal principal) {
		
		try {			
			Optional<User> user = userService.findUserNickname(principal.getName());
			long followerlistsize=userService.followerlist(user).size();
			long followlistsize = userService.followlist(user).size();
			if(user.get().getUserProfileImage()==null) {//image notfound
				return ResponseEntity.ok(new UserResponse(user.get(),followerlistsize,followlistsize));//유저 프로필이미지가 없는 경우  
			}else {
				return ResponseEntity.ok(new UserProfileResponse(user.get(),followerlistsize,followlistsize));	//유저 프로필이미지가 있는경우
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	/*다른사람 정보불러오기*/
	@GetMapping("/user/otheruserInfo/{id}")
		public ResponseEntity<?> otheruserInfo(@PathVariable long id) {
		
		try {			
			Optional<User> user = userService.findUserId(id);
			long followerlistsize=userService.followerlist(user).size();
			long followlistsize = userService.followlist(user).size();
			if(user.get().getUserProfileImage()==null) {//image notfound
				return ResponseEntity.ok(new UserResponse(user.get(),followerlistsize,followlistsize));//유저 프로필이미지가 없는 경우  
			}else {
				return ResponseEntity.ok(new UserProfileResponse(user.get(),followerlistsize,followlistsize));	//유저 프로필이미지가 있는경우
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	/*내 팔로워리스트 불러오기 2020-09-26 choiseongjun
	 * DTO로 변환필요..
	 * */
	@GetMapping("/user/followerlist/{id}")
	public ResponseEntity<?> followerlist(@PathVariable long id){
		try {
			Optional<User> user = userService.findUserId(id);
//			Optional<User> user = userService.findUserNickname(principal.getName());
			List<Follow> followlist=userService.followerlist(user);
			return new ResponseEntity<>(followlist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	/*내 팔로잉리스트 불러오기 2020-11-03 choiseongjun
	 * DTO로 변환필요..
	 * */
	@GetMapping("/user/followinglist/{id}")
	public ResponseEntity<?> followinglist(@PathVariable long id){
		try {
			Optional<User> user = userService.findUserId(id);
			List<Follow> followlist=userService.followinglist(user);
			return new ResponseEntity<>(followlist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	/*id는 팔로잉당하는사람*/
	@PostMapping("/user/following/{id}")
	public ResponseEntity<?> following(@PathVariable long id,Principal principal){
	
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			User users = userService.following(id,user);

			FeedLike feedlike =new FeedLike();
				for(Follow f:users.getFollow()) {
					if(f.getFromUser().getId()==user.get().getId()) {
						feedlike.setUserkey(users.getId());
						feedlike.setTempFollow(true);//임시변수로 팔로우한지 안한지 처리
				}
			}

			return new ResponseEntity<>(feedlike,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/user/following/{id}")
	public ResponseEntity<?> canclefollowing(@PathVariable long id,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			User users =userService.canclefollowing(id,user);
			
			FeedLike feedlike =new FeedLike();
			feedlike.setTempFollow(false);
			feedlike.setUserkey(id);
			return new ResponseEntity<>(feedlike,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
		
	}
	/*네비게이션 바에서 유저검색창
	 * 2020.11.04
	 * */
	@GetMapping("/user/userSearch/{nickname}")
	public ResponseEntity<?> userSearch(@PathVariable String nickname){
		
		try {
			List<User> user = userService.searchUserNickname(nickname);	
			return new ResponseEntity<>(user,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	
}
