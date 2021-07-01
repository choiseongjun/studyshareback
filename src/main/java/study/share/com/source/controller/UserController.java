package study.share.com.source.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import study.share.com.source.message.response.UserProfileResponse;
import study.share.com.source.message.response.UserResponse;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.AuthTokenDTO;
import study.share.com.source.model.DTO.FollowDTO;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.service.AuthTokenService;
import study.share.com.source.service.BlockedUserService;
import study.share.com.source.service.feed.FeedListService;
import study.share.com.source.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	FeedListService feedListService;
	@Autowired
	UserRepository userRepository;
	@Autowired
    AuthTokenService authTokenService;
	@Autowired
	BlockedUserService blockedUserService;
	
	@ApiOperation(value="내정보 불러오기",notes="내정보 불러오기")
	@GetMapping("/api/auth/userinfo")
	public ResponseEntity<?> userAccess(Principal principal,Pageable pageable) {
		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다

			Optional<User> user = userService.findUserNickname(principal.getName());
			long followerlistsize=userService.followerlist(user,pageable).getTotalElements();
			long followlistsize = userService.followlist(user,pageable).getTotalElements();
			List<Follow> followlist = user.get().getFollow();
			
			List<FeedList> feedList = feedListService.FindFeedUser(user.get());
			long feedTotalCnt = feedList.stream().filter(value->value.getDeleteyn()=='N').count();
			
					 
			List<BlockedUser> blockUserList=blockedUserService.findBlockUserList(user.get());
			
			if(user.get().getUserProfileImage()==null) {//image notfound
				return ResponseEntity.ok(new UserResponse(user.get(),followlist,followerlistsize,followlistsize,blockUserList,feedTotalCnt));//유저 프로필이미지가 없는 경우
			}else {
				return ResponseEntity.ok(new UserProfileResponse(user.get(),followlist,followerlistsize,followlistsize,blockUserList,feedTotalCnt));	//유저 프로필이미지가 있는경우
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	@ApiOperation(value="내정보 수정",notes="내정보 수정")
	@PatchMapping("/user/updateUser")
	public ResponseEntity<?> updateUser(@RequestBody User userInfo,Principal principal){


		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			User returnUser = userService.updateUserInfo(user,userInfo);
	        AuthTokenDTO authTokenDTO = authTokenService.createAuthToken(returnUser.getNickname());			
			return new ResponseEntity<>(new UserProfileResponse(returnUser,authTokenDTO.getAccessToken(),authTokenDTO.getRefreshToken()),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	/*다른사람 정보불러오기*/
	@ApiOperation(value="다른사람 정보불러오기",notes="다른사람 정보불러오기")
	@GetMapping("/user/otheruserInfo/{id}")
		public ResponseEntity<?> otheruserInfo(@PathVariable long id,Principal principal,Pageable pageable) {
		try {
			if(principal==null) {
				Optional<User> user = userService.findUserId(id);//다른사람 계정 조회
	
				long followerlistsize=userService.followerlist(user,pageable).getTotalElements();
				long followlistsize = userService.followlist(user,pageable).getTotalElements();
				List<Follow> followlist = user.get().getFollow();
				long followCheck = 0;
			

				List<FeedList> feedList = feedListService.FindFeedUser(user.get());
				long feedTotalCnt = feedList.stream().filter(value->value.getDeleteyn()=='N').count();

				if(user.get().getUserProfileImage()==null) {//image notfound
					return ResponseEntity.ok(new UserResponse(user.get(),followlist,followerlistsize,followlistsize,feedTotalCnt,followCheck));//유저 프로필이미지가 없는 경우
				}else {
					return ResponseEntity.ok(new UserProfileResponse(user.get(),followlist,followerlistsize,followlistsize,feedTotalCnt,followCheck));	//유저 프로필이미지가 있는경우
				}	
			}else {

				Optional<User> fromUser = userService.findUserNickname(principal.getName());//내 계정 조회
				Optional<User> user = userService.findUserId(id);//다른사람 계정 조회
				if(!user.isPresent())//찾고자 하는 유저가 존재하지 않는 경우
					return new ResponseEntity<>("해당 유저가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
				long followerlistsize=userService.followerlist(user,pageable).getTotalElements();
				long followlistsize = userService.followlist(user,pageable).getTotalElements();
				List<Follow> followlist = user.get().getFollow();
				long followCheck = 0;
				Optional <Follow> followResult=userService.findfollowing(id,fromUser.get());//팔로우 정보 조회
				//System.out.println("팔로우 당함: "+followResult.get().getToUser().getId()+"팔로우 함: "+followResult.get().getFromUser().getId());
				if(followResult.isPresent())
					followCheck =1L;

				List<FeedList> feedList = feedListService.FindFeedUser(user.get());
				long feedTotalCnt = feedList.stream().filter(value->value.getDeleteyn()=='N').count();
				if(user.get().getUserProfileImage()==null) {//image notfound
					System.out.println("값 확인 사진 없음 : "+followCheck);
					return ResponseEntity.ok(new UserResponse(user.get(),followlist,followerlistsize,followlistsize,feedTotalCnt,followCheck));//유저 프로필이미지가 없는 경우
				}else {
					System.out.println("값 확인: "+followCheck);
					return ResponseEntity.ok(new UserProfileResponse(user.get(),followlist,followerlistsize,followlistsize,feedTotalCnt,followCheck));	//유저 프로필이미지가 있는경우
				}	
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("error",HttpStatus.BAD_REQUEST);
		}
	}
	/*내 팔로워리스트 불러오기 2020-09-26 choiseongjun
	 * DTO로 변환필요..
	 * */
	//나를 팔로우 하는 사람
	@ApiOperation(value="내 팔로워리스트 불러오기",notes="내 팔로워리스트 불러오기")
	@GetMapping("/user/followerlist/{id}")
	public ResponseEntity<?> followerlist(@PathVariable long id, Pageable pageable){
		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Optional<User> user = userService.findUserId(id);
			Page<Follow> followlist=userService.followerlist(user,pageable);
			return new ResponseEntity<>(followlist.stream().map(FollowDTO::new),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	/*내 팔로잉리스트 불러오기 2020-11-03 choiseongjun
	 * DTO로 변환필요..
	 * */

	//내가 팔로우 하는 사람
	@ApiOperation(value="내 팔로잉리스트 불러오기",notes="내 팔로잉리스트 불러오기")
	@GetMapping("/user/followinglist/{id}")
	public ResponseEntity<?> followinglist(@PathVariable long id,Pageable pageable){
		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Optional<User> user = userService.findUserId(id);
			Page<Follow> followlist=userService.followinglist(user,pageable);
			return new ResponseEntity<>(followlist.stream().map(FollowDTO::new),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	/*id는 팔로잉당하는사람*/
	@ApiOperation(value="팔로우 하기",notes="팔로우 하기")
	@PostMapping("/user/following/{id}")
	public ResponseEntity<?> following(@PathVariable long id,Principal principal){

		Optional<User> user = userService.findUserNickname(principal.getName());
		Optional <Follow> followResult=userService.findfollowing(id,user.get());//팔로우 정보 조회
		if(followResult.isPresent())
			return new ResponseEntity<>("이미 팔로우 하고 있습니다",HttpStatus.BAD_REQUEST);
		Follow follow = userService.following(id,user.get());

		return new ResponseEntity<>(new FollowDTO(follow),HttpStatus.OK);

	}
	/*id는 팔로잉삭제*/
	@ApiOperation(value="팔로잉삭제",notes="팔로잉삭제")
	@DeleteMapping("/user/following/{id}")
	public ResponseEntity<?> canclefollowing(@PathVariable long id,Principal principal){

		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			User users =userService.canclefollowing(id,user);


			return new ResponseEntity<>("삭제 완료",HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
		
	}
	/*네비게이션 바에서 유저검색창
	 * 2020.11.04
	 * */
	@ApiOperation(value="유저검색",notes="유저검색")
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
	@ApiOperation(value="아이디 중복체크",notes="아이디 중복체크")
	@GetMapping("/user/emailcheck/{userId}")
	public ResponseEntity<?> emailcheck(@PathVariable String userId){
		
		try {
			if(userRepository.existsByUserid(userId)) {
	            return new ResponseEntity<String>("아이디가 이미 존재합니다!",
	                    HttpStatus.BAD_REQUEST);
	        }else {
	        	return new ResponseEntity<String>("사용 가능한 아이디입니다.",
	                    HttpStatus.OK);	        }
		}catch(Exception e) {
			return new ResponseEntity<>("서버 오류입니다.새로고침 후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
		
	}
}
