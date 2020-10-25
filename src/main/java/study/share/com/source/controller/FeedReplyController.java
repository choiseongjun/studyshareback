package study.share.com.source.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.service.FeedReplyService;
import study.share.com.source.service.UserService;

@RestController
public class FeedReplyController {

	@Autowired
	FeedReplyService feedReplyService;
	@Autowired
	UserService userService;
	
	@PostMapping("feed/reply/{id}")
	public ResponseEntity<?> addfeedcomment(@PathVariable long id,Principal principal,@RequestBody Map<String, String> data){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			String content = data.get("content");
			FeedReply feedReplylist=feedReplyService.addfeedcomment(id,user,content);
			
			return new ResponseEntity<>(feedReplylist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@PatchMapping("feed/reply/{id}")
	public ResponseEntity<?> updatefeedcomment(@PathVariable long id,Principal principal,@RequestBody Map<String, String> data){
		try {
			if(principal==null) {
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.BAD_REQUEST);
			}
			Optional<User> user = userService.findUserNickname(principal.getName());
			User replyuser = feedReplyService.checkusercomment(id);//댓글작성자랑 로그인한 유저가 같은지 비교
			if(user.get().getId()!=replyuser.getId()) {
				return new ResponseEntity<>("작성자가 다릅니다",HttpStatus.BAD_REQUEST);	
			}else {
				String content = data.get("content");
				FeedReply feedReplylist=feedReplyService.updatefeedcomment(id,user,content);
				
				return new ResponseEntity<>(feedReplylist,HttpStatus.OK);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	
	@DeleteMapping("feed/reply/{id}")
	public ResponseEntity<?> removefeedcomment(@PathVariable("id") long id,Principal principal){
		
		try {
			if(principal==null) {
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.BAD_REQUEST);
			}
			
			Optional<User> user = userService.findUserNickname(principal.getName());
			User replyuser = feedReplyService.checkusercomment(id);//댓글작성자랑 로그인한 유저가 같은지 비교
			if(user.get().getId()!=replyuser.getId()) {
				return new ResponseEntity<>("작성자가 다릅니다",HttpStatus.BAD_REQUEST);	
			}else {
				feedReplyService.removefeedcomment(id,user);	
				long postId = feedReplyService.findPostId(id);
				Map<String, Long> data=new HashMap<String, Long>();
				data.put("commentId", id);
				data.put("postId",postId);
				return new ResponseEntity<>(data,HttpStatus.OK);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("feed/reply/{id}")
	public ResponseEntity<?> getfeedreply(@PathVariable long id){
		try {
			
			List<FeedReply> feedReplylist=feedReplyService.getfeedreply(id);
			feedReplylist.sort(new Comparator<FeedReply>() {//내림차순 로직
				@Override
				public int compare(FeedReply o1, FeedReply o2) {
					long id1 = o1.getId();
					long id2 = o2.getId();
					if(id2>id1)
						return 1;
					else
						return -1;
				}
			});
			return new ResponseEntity<>(feedReplylist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
}
