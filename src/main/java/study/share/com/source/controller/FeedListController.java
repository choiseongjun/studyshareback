package study.share.com.source.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import study.share.com.source.model.FeedList;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.FeedListDTO;
import study.share.com.source.model.DTO.FeedListLikeDTO;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.service.FeedListService;
import study.share.com.source.service.UserService;

@RestController
public class FeedListController {

	@Autowired
	UserService userService;
	@Autowired
	FeedListService feedListService;
	@Autowired
	FeedListRepository feedListRepository;
	
	@PostMapping("/feed")
	public ResponseEntity<?> savefeed(@RequestParam(name = "images", required = false) String file
			,@RequestPart(name = "content", required = false) String content,Principal principal) throws IOException {
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedList> feedlist=feedListService.saveFeed(user,content,file);
			
			return new ResponseEntity<>(feedlist.get(),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);
		}
		
	}
	@GetMapping("/feed")
	public ResponseEntity<?> listfeed(){
		 
		try {
			List<FeedList> feedlist = feedListService.listfeed(); 
			//new FeedListLikeDTO(feedList.get()
			//return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);
			return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);
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
	
	@DeleteMapping("/feed/{id}")
	public ResponseEntity<?> deletefeed(@PathVariable long id){
		try {
			feedListService.deletefeed(id); 
			return new ResponseEntity<>(id,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	/*좋아요 */
	@PostMapping("/likefeed/{id}")
	public ResponseEntity<?> likefeed(@PathVariable long id,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedList> feedList = feedListService.likefeed(user,id);
			return new ResponseEntity<>(new FeedListLikeDTO(feedList.get()),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	/*좋아요 취소*/
	/* 좋아요 감소하는거 로직 문제있음..*/
	@DeleteMapping("/likefeed/{id}")
	public ResponseEntity<?> dislikefeed(@PathVariable long id,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedList> feedList = feedListService.dislikefeed(user,id);
			return new ResponseEntity<>(new FeedListLikeDTO(feedList.get()),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	
}
