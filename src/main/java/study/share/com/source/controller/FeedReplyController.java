package study.share.com.source.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import study.share.com.source.model.DTO.FeedListLikeDTO;
import study.share.com.source.model.DTO.FeedReplyDTO;
import study.share.com.source.model.DTO.FeedReplyLikeDTO;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.FeedReplyLike;
import study.share.com.source.model.User;
import study.share.com.source.service.FeedReplyService;
import study.share.com.source.service.UserService;

@RestController
public class FeedReplyController {

	@Autowired
	FeedReplyService feedReplyService;
	@Autowired
	UserService userService;
	
	@ApiOperation(value="게시글별 댓글 작성",notes="게시글별 댓글 작성")
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
	@ApiOperation(value="게시글별 댓글 수정",notes="게시글별 댓글 수정")
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
	@ApiOperation(value="게시글별 댓글 삭제",notes="게시글별 댓글 삭제")
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
	
	@ApiOperation(value="게시글별 댓글 조회",notes="게시글별 댓글 조회")
	@GetMapping("feed/reply/{id}")
	public ResponseEntity<?> getfeedreply(@PathVariable long id, @RequestParam("page")int page, @RequestParam("size")int size){
		try {
//			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
//			pageable = PageRequest.of(page, 0, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			//pageable = PageRequest.of(0, 10, Sort.by("Id").descending());
			//Map<String, Object> map =new HashMap<String, Object>();
			//FeedList feedlist=new FeedList();
			//feedlist.setId(id);

			Page<FeedReply> feedReplylist=feedReplyService.getfeedreply(id,PageRequest.of(page, size, Sort.Direction.DESC, "id"));
			List<FeedReplyDTO> feedReplyDTOList =new ArrayList<>();
			feedReplylist.stream()
					.filter(feedReply -> feedReply != null)
					.forEach(feedReply -> {
						feedReplyDTOList.add(new FeedReplyDTO(feedReply));
					});
			//map.put("feedReplylist", feedReplylist);
			//map.put("feedlist",feedlist);
			return new ResponseEntity<>(feedReplyDTOList,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}

	@ApiOperation(value="댓글 좋아요",notes="댓글 좋아요")
	@PostMapping("/likefeedreply/{id}")
	public ResponseEntity<?> likefeedreply(@PathVariable long id,Principal principal){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			FeedReplyLikeDTO feedReplylike = feedReplyService.likefeedreply(user,id);
			return new ResponseEntity<>(feedReplylike,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	@ApiOperation(value="댓글 좋아요 취소",notes="댓글 좋아요 취소 ")
	@DeleteMapping("/likefeedreply/{id}")
	public ResponseEntity<?> likefeedreplyCancle(@PathVariable long id,Principal principal){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			feedReplyService.likeCanclefeedreply(user,id);
			return new ResponseEntity<>("댓글 좋아요 취소되었습니다",HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="댓글의 댓글 작성",notes="댓글의 댓글 작성")
	@PostMapping("feed/reply/re/{feedid}/{id}")
	public ResponseEntity<?> addfeedcommentreply(@PathVariable long feedid,@PathVariable long id,Principal principal,@RequestBody Map<String, String> data){
		Optional<User> user = userService.findUserNickname(principal.getName());
		String content = data.get("content");
		FeedReply feedReplylist=feedReplyService.addfeedcommentReply(feedid,id,user,content);
		FeedReplyDTO feedReplyDTO=new FeedReplyDTO(feedReplylist);
		try {
	
			return new ResponseEntity<>(feedReplyDTO,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
}
