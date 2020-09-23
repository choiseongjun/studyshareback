package study.share.com.source.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.User;
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
			return new ResponseEntity<>(feedlist,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@GetMapping("/testfeed")
	public ResponseEntity<?> test(){ 
		Optional<FeedList> feedlist= feedListRepository.findById(17L);
		return new ResponseEntity<>(feedlist.get(),HttpStatus.OK);
	}
}
