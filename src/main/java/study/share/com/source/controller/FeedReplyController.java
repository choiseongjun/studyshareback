package study.share.com.source.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.FeedList;
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
			FeedList feedReplylist=feedReplyService.addfeedcomment(id,user,content);
			return new ResponseEntity<>(feedReplylist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	
	@DeleteMapping("feed/reply/{id}")
	public ResponseEntity<?> removefeedcomment(@PathVariable long id,Principal principal){
		
		try {
			if(principal==null) {
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.BAD_REQUEST);
			}
			
			Optional<User> user = userService.findUserNickname(principal.getName());
			User replyuser = feedReplyService.checkusercomment(id);//댓글작성자랑 로그인한 유저가 같은지 비교
//			if(user.get().getId()!=replyuser.getId()) {
//				return new ResponseEntity<>("작성자가 다릅니다",HttpStatus.BAD_REQUEST);	
//			}else {
//				feedReplyService.removefeedcomment(id,user);	
//				return new ResponseEntity<>(id,HttpStatus.OK);
//			}
			feedReplyService.removefeedcomment(id,user);	
			return new ResponseEntity<>(id,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	
	/*choiseongjun 일단 안씀 피드리스트에서 한번에 조회해서 들고오기때문에 그걸로 조회중..*/
//	@GetMapping("feed/reply/{id}")
//	public ResponseEntity<?> getfeedreply(@PathVariable long id){
//		try {
//			
//			List<FeedReply> feedReplylist=feedReplyService.getfeedreply(id);
//			return new ResponseEntity<>(feedReplylist,HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	}
}
