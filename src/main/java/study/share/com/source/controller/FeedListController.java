package study.share.com.source.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.Follow;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.FeedListDTO;
import study.share.com.source.model.DTO.FeedListLikeDTO;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.service.FeedListService;
import study.share.com.source.service.UserService;
import study.share.com.source.utils.HashTagExtract;

@RestController
public class FeedListController {

	@Autowired
	UserService userService;
	@Autowired
	FeedListService feedListService;
	@Autowired
	FeedListRepository feedListRepository;
	@Autowired
	HashTagExtract hashTagExtract;
	
	@ApiOperation(value="피드리스트 작성",notes="피드리스트 작성")
	@PostMapping("/feed")
	public ResponseEntity<?> savefeed(@RequestParam(name = "images", required = false) String file
			,@RequestPart(name = "content", required = false) String content,Principal principal) throws IOException {
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			FeedList feedlist=feedListService.saveFeed(user,content,file);
			//long feedid =feedListService.saveFeed(user,content,file);
			//Optional<FeedList> feedlist = feedListService.selectOne(feedid); 
			return new ResponseEntity<>(new FeedListDTO(feedlist),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
		}
		
	}
	@PostMapping("/testfeed")
	public void savetestfeed(@RequestPart(name = "content", required = false) String content) {
		System.out.println(content);
		List<String> result = hashTagExtract.extractHashTagTest(content);
		if(!result.isEmpty()) {//해시태그가 있다면..
			for(int i=0;i<result.size();i++) {
				System.out.println(result.get(i));
			}
		}
		
		
	}
	@ApiOperation(value="피드리스트 조회",notes="피드리스트 조회")
	@GetMapping("/feed")
	public ResponseEntity<?> listfeed(Pageable pageable){
		 
		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Page<FeedList> feedlist = feedListService.listfeed(pageable);
			return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);
		}catch(Exception e) {  
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@ApiOperation(value="피드리스트 상세조회",notes="피드리스트 상세조회")
	@GetMapping("/feedDetail/{id}")
	public ResponseEntity<?> listfeedDetail(@PathVariable long id){
		 
		try {
			FeedList feedlist = feedListService.listfeedDetail(id);
			return new ResponseEntity<>(feedlist,HttpStatus.OK);
		}catch(Exception e) {  
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@GetMapping("/gallary")
	public ResponseEntity<?> listgallary(){
		
		try {
			List<UploadFile> gallarylist = feedListService.listgallary();
			return new ResponseEntity<>(gallarylist,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@ApiOperation(value="피드 수정",notes="피드 수정")
	@PatchMapping("/feed/{id}")
	public ResponseEntity<?> updatefeed(@PathVariable long id,@RequestBody Map<String, String> data){
		try {
			String content = data.get("content");
			Optional<FeedList> feedlist = feedListService.updatefeed(id,content); 
			return new ResponseEntity<>(feedlist,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@ApiOperation(value="피드 삭제",notes="피드 삭제")
	@DeleteMapping("/feed/{id}")
	public ResponseEntity<?> deletefeed(@PathVariable long id){
		try {
			feedListService.deletefeed(id); 
			return new ResponseEntity<>(id,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	/*좋아요 */
	@ApiOperation(value="좋아요",notes="좋아요")
	@PostMapping("/likefeed/{id}")
	public ResponseEntity<?> likefeed(@PathVariable long id,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedList> feedList = feedListService.likefeed(user,id);
			FeedListLikeDTO feedListLike=new FeedListLikeDTO(feedList.get());
			feedListLike.setUserKey(user.get().getId());
			return new ResponseEntity<>(feedListLike,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	/*좋아요 취소*/
	@ApiOperation(value="좋아요 취소",notes="좋아요 취소")
	@DeleteMapping("/likefeed/{id}")
	public ResponseEntity<?> dislikefeed(@PathVariable long id,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedList> feedList = feedListService.dislikefeed(user,id);
			FeedListLikeDTO feedListLike=new FeedListLikeDTO(feedList.get());
			feedListLike.setUserKey(user.get().getId());
			return new ResponseEntity<>(feedListLike,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	/*게시글별 좋아요리스트 조회*/
	@ApiOperation(value="게시글별 좋아요리스트 조회",notes="게시글별 좋아요리스트 조회")
	@GetMapping("/feed/likefeedlist/{id}")
	public ResponseEntity<?> likefeedlist(@PathVariable long id,Principal principal){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			List<FeedLike> feedlike=feedListService.selectFeedlikelist(id);
			for(FeedLike feedlikefollow : feedlike) {
				for(Follow f:feedlikefollow.getUser().getFollow()) {
					if(f.getFromUser().getId()==user.get().getId()) {
						//좋아요 유저의 팔로우정보를 들고와서 from으로부터 한 아이디와 내정보아이디를 비교한다 맞으면 true
						feedlikefollow.setTempFollow(true);//임시변수로 팔로우한지 안한지 처리
					}
				}
			}
			return new ResponseEntity<Object>(feedlike,HttpStatus.OK);
		}catch(Exception e) {  
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}

	@ApiOperation(value="내 피드 조회",notes="내 피드 조회")
	@GetMapping("/feed/my")
	public ResponseEntity<?> mylistfeed(Pageable pageable,Principal principal){

		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Page<FeedList> feedlist = feedListService.mylistfeed(pageable,user.get().getId());
			return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="다른사람 피드 조회",notes="다른사람 피드 조회")
	@GetMapping("/feed/other/{id}")
	public ResponseEntity<?> otherlistfeed(Pageable pageable,@PathVariable long id){

		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Page<FeedList> feedlist = feedListService.otherlistfeed(pageable,id);
			return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
//	/*게시글별 좋아요리스트 조회*/
//	@GetMapping("/feed/likefeedlistandfollow/{id}")
//	public ResponseEntity<?> likefeedlist(@PathVariable long id,Principal principal){
//		//List<FeedLike> feedlike=feedLikeRepository.findByfeedlistId(id);
//		try {
//			Optional<User> user = userService.findUserNickname(principal.getName());
//			//FeedList feedlikelist=feedListRepository.findById(id).get();
//			List<FeedLike> feedlike=feedListService.selectFeedlikelist(id);
//			
//			return new ResponseEntity<Object>(new FeedLikeListDTO(feedlike),HttpStatus.OK);
//		}catch(Exception e) {  
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	}
}
