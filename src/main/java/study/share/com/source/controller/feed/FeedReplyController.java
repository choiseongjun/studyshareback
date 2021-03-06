package study.share.com.source.controller.feed;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import study.share.com.source.controller.AlarmController;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.feed.FeedReplyDTO;
import study.share.com.source.model.DTO.feed.FeedReplyLikeDTO;
import study.share.com.source.model.report.ReportFeedReply;
import study.share.com.source.repository.feed.FeedReplyLikeRepository;
import study.share.com.source.repository.feed.FeedReplyRepository;
import study.share.com.source.repository.report.ReportFeedReplyRepository;
import study.share.com.source.service.feed.FeedReplyService;
import study.share.com.source.service.report.ReportFeedReplyService;
import study.share.com.source.service.UserService;

@RestController
public class FeedReplyController {

	@Autowired
	FeedReplyService feedReplyService;
	@Autowired
	UserService userService;
	@Autowired
	FeedReplyLikeRepository feedReplyLikeRepository;
	@Autowired
	AlarmController alarmController;
	@Autowired
	ReportFeedReplyRepository reportFeedReplyRepository;
	@Autowired
	FeedReplyRepository feedReplyRepository;
	@Autowired
	ReportFeedReplyService reportFeedReplyService;
	
	public static List<FeedReply> replyContnet = new ArrayList<FeedReply>();//대댓글 쓸때 필요함 2021 04-18 choiseongjun
	public static List<FeedReply> feedReplyUser = new ArrayList<FeedReply>();//댓글조회해서 내가 좋아요누른거 가져오기

	
	@ApiOperation(value="게시글별 댓글 작성",notes="게시글별 댓글 작성")
	@PostMapping("feed/reply/{id}")
	public ResponseEntity<?> addfeedcomment(@PathVariable long id,Principal principal,@RequestBody Map<String, String> data){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			String content = data.get("content");
			FeedReply feedReplylist=feedReplyService.addfeedcomment(id,user,content);

			alarmController.alertreply(feedReplylist, id);//댓글 생성 알림

			return new ResponseEntity<>(new FeedReplyDTO(feedReplylist),HttpStatus.OK);
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
				
				return new ResponseEntity<>(new FeedReplyDTO(feedReplylist),HttpStatus.OK);
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
	public ResponseEntity<?> getfeedreply(@PathVariable long id,Pageable pageable,Principal principal){
//		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
//		pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
		
		if(principal==null) {
			Page<FeedReply> feedReplylist=feedReplyService.getfeedreply(id,pageable);
			List<FeedReply> feedReplylist2 = feedReplylist.getContent();
			List<Long> feedreplyId = feedReplylist.stream().map(t->t.getId()).collect(Collectors.toList());
			
			
			for(int i=0;i<feedreplyId.size();i++) {
				Page<FeedReply> feedReReplylist=feedReplyService.getfeedrereply(feedreplyId.get(i),pageable); 
				replyContnet= feedReReplylist.getContent();
			}	
//			if(replyContnet.size()>0) {
//				return new ResponseEntity<>(feedReplylist2.stream().map(t->new FeedReplyDTO(t,replyContnet)),HttpStatus.OK);
//				
//			}else {
//				return new ResponseEntity<>(feedReplylist2.stream().map(t->new FeedReplyDTO(t)),HttpStatus.OK);
//
//			}
			return new ResponseEntity<>(feedReplylist.stream().map(t->new FeedReplyDTO(t)),HttpStatus.OK);


		}else {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Page<FeedReply> feedReplylist=feedReplyService.getfeedreply(id,pageable);
			List<FeedReply> feedReplylist2 = feedReplylist.getContent();
			List<Long> feedreplyId = feedReplylist.stream().map(t->t.getId()).collect(Collectors.toList());
//			Page<FeedReply> feedReplyUser = (Page<FeedReply>) new ArrayList<FeedReply>();//댓글조회해서 내가 좋아요누른거 가져오기
			for(int i=0;i<feedreplyId.size();i++) {
				Page<FeedReply> feedReReplylist=feedReplyService.getfeedrereply(feedreplyId.get(i),pageable); 
				replyContnet= feedReReplylist.getContent();
			}
//			Page<FeedReply> feedReplylist3 = feedReplyService.feedReplyFeedLikeUserFind(id,user.get(),pageable);
//			feedReplyUser = feedReplylist3.getContent();
			return new ResponseEntity<>(feedReplylist.stream().map(t->new FeedReplyDTO(t,user.get())),HttpStatus.OK);

//			if(replyContnet.size()>0) {
//				return new ResponseEntity<>(feedReplylist2.stream().map(t->new FeedReplyDTO(t,replyContnet,user.get())),HttpStatus.OK);
//				
//			}else {
//				return new ResponseEntity<>(feedReplylist2.stream().map(t->new FeedReplyDTO(t,user.get())),HttpStatus.OK);
//
//			}

	

		}

		


//		try {
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
	}

	@ApiOperation(value="댓글 좋아요",notes="댓글 좋아요")
	@PostMapping("/likefeedreply/{id}")
	public ResponseEntity<?> likefeedreply(@PathVariable long id,Principal principal){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedReplyLike> userResult=feedReplyLikeRepository.findByFeedReplyIdAndUserId(id,user.get().getId());
			if(userResult.isPresent()) {
				return new ResponseEntity<>("이미 좋아요를 눌렀습니다.",HttpStatus.BAD_REQUEST);	

			}
			FeedReplyLikeDTO feedReplylike = feedReplyService.likefeedreply(user,id);

			
			
			return new ResponseEntity<>(feedReplylike,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	@ApiOperation(value="댓글 좋아요 취소",notes="댓글 좋아요 취소 ")
	@DeleteMapping("/likefeedreply/{id}")
	public ResponseEntity<?> likefeedreplyCancel(@PathVariable long id,Principal principal){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			feedReplyService.likeCanclefeedreply(user,id);
			FeedReplyLikeDTO feedReplylike = new FeedReplyLikeDTO();
			
			return new ResponseEntity<>(feedReplylike,HttpStatus.OK);
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

	@ApiOperation(value="댓글 신고",notes="댓글 신고")
	@PostMapping("/report/feedReply/{id}/{reportId}")
	public ResponseEntity<?> reportFeeduser(@PathVariable long reportId,@PathVariable long id
			,Principal principal){
		Optional <User> reporter = userService.findUserNickname(principal.getName());
		Optional<FeedReply> reportfeedReply = feedReplyRepository.findById(id);
		int result =0;
		if(reportfeedReply.isPresent()) {
			result=reportFeedReplyService.reportFeedReplySave(reportfeedReply.get(), reportId, reporter.get());
		}
		if (result==0)
			return new ResponseEntity<>("피드 신고 성공",HttpStatus.OK);
		else
			return new ResponseEntity<>("이미 신고한 피드 입니다",HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value="댓글 신고 취소",notes="댓글 신고 취소")
	@DeleteMapping("/report/feedReply/{id}")
	public ResponseEntity<?> reportFeedDelete(@PathVariable long id,Principal principal){
		try {
			Optional<FeedReply> reportfeedReply = feedReplyRepository.findById(id);
			Optional <User> reporter = userService.findUserNickname(principal.getName());
			if(reportfeedReply.isPresent()) {
				reportFeedReplyService.reportFeedReplyDelete(reportfeedReply.get(),reporter.get());
			}
			return new ResponseEntity<>("피드 신고 삭제 성공",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="피드 신고 조회",notes="피드 신고 조회")
	@GetMapping("/report/feedReply")
	public ResponseEntity<?> reportFeedView(Pageable pageable){
		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Page<ReportFeedReply> reportFeedPage =reportFeedReplyService.reportfeedlist(pageable);
			return new ResponseEntity<>(reportFeedPage,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}



}
