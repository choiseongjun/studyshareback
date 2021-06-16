package study.share.com.source.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import study.share.com.source.model.*;
import study.share.com.source.model.DTO.FeedListDTO;
import study.share.com.source.model.DTO.FeedListLikeDTO;
import study.share.com.source.model.report.ReportFeed;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.ReportFeedRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.service.BlockedUserService;
import study.share.com.source.service.FeedListService;
import study.share.com.source.service.ReportFeedService;
import study.share.com.source.service.UserService;
import study.share.com.source.utils.HashTagExtract;

@RestController
public class FeedListController {

	@Autowired
	UserService userService;
	@Autowired
	FeedListService  feedListService;
	@Autowired
	FeedListRepository feedListRepository;
	@Autowired
	HashTagExtract hashTagExtract;
	@Autowired
	AlarmController alarmController;
	@Autowired
	ReportFeedService reportFeedService;
	@Autowired
	ReportFeedRepository reportFeedRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BlockedUserService blockedUserService;

	
	@ApiOperation(value="피드리스트 작성",notes="피드리스트 작성")
	@PostMapping("/feed")
	public ResponseEntity<?> savefeed(@RequestParam(name = "images", required = false) String file
			,@RequestParam(name = "content", required = false) String content,Principal principal) throws IOException {
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());

			String eraseTag=feedListService.remakeTag(content);
			FeedList feedlist=feedListService.saveFeed(user,eraseTag,file);
			if(eraseTag!=null)
				feedListService.extractHashTag(content,feedlist);//해시태그 검출 및 저장-> 테이블 사용시 다시

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
		List<String> result = hashTagExtract.extractHashTagTest(content);
		if(!result.isEmpty()) {//해시태그가 있다면..
			for(int i=0;i<result.size();i++) {
				System.out.println(result.get(i));
			}
		}
		
		
	}
	@ApiOperation(value="피드리스트 조회",notes="피드리스트 조회")
	@GetMapping("/feed")
	public ResponseEntity<?> listfeed(Pageable pageable,Principal principal){
		if(principal==null) {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Page<FeedList> feedlist = feedListService.listfeed(pageable);
			return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);	
		}else {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Optional<User> user = userService.findUserNickname(principal.getName());
			List<BlockedUser> findBlocked =blockedUserService.findReportList(user.get().getId());//차단된 사용자 반환
			Page<FeedList> feedMylike ;
			List blocked = new ArrayList <>();
			if(!findBlocked.isEmpty())
			{
				for(BlockedUser object: findBlocked)//차단 사용자 아이디 추출
				{
					blocked.add(object.getBlockedUser().getId());
					System.out.println("차단 된 사용자 조회: " +object.getBlockedUser().getId());
				}
				feedMylike=feedListService.feedMylike(pageable,user,blocked);
			}
			else {//차단 하는 사용자가 없는 경우
				feedMylike = feedListService.feedMylikeNotBlock(pageable,user);
			}
			List <FeedList> de = feedMylike.getContent();
			for(FeedList obj : de)
			{
				System.out.println("결과물 조회: "+obj.getUser().getId());
			}
			return new ResponseEntity<>(feedMylike.stream().map(t->new FeedListDTO(t,user.get())),HttpStatus.OK);
		}	 
//		try {
//	
//			
//		}catch(Exception e) {  
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
	}
	@ApiOperation(value="피드리스트 상세조회",notes="피드리스트 상세조회")
	@GetMapping("/feedDetail/{id}")
	public ResponseEntity<?> listfeedDetail(@PathVariable long id,Principal principal){

			if(principal==null) {
				FeedList feedlist = feedListService.listfeedDetail(id);
				return new ResponseEntity<>(new FeedListDTO(feedlist),HttpStatus.OK);

			}else {
				Optional<User> user = userService.findUserNickname(principal.getName());
				Optional<FeedList> feedlist = feedListService.listMyFeedLikeFeedDetail(id,user);
				if(!user.isPresent())
					return new ResponseEntity<>("해당 사용자가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
				if(!feedlist.isPresent())
					return new ResponseEntity<>("해당 사용자의 피드가 존재하지 않습니다",HttpStatus.BAD_REQUEST);
				return new ResponseEntity<>(new FeedListDTO(feedlist.get(),user.get()),HttpStatus.OK);
				//return new ResponseEntity<>(feedlist.stream().map(t->new FeedListDTO(t,user.get())),HttpStatus.OK);
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
	public ResponseEntity<?> updatefeed(
			@RequestParam(name = "images", required = false) String file,
			@PathVariable long id,
			@RequestParam(name = "content", required = false) String content,
			Principal principal){

		try {
//			String content = data.get("content");
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<FeedList> feedlist = feedListService.updatefeed(id,content,file);

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
//    @SendTo("/alert/feedlike")
	@PostMapping("/likefeed/{id}")

	public ResponseEntity<?> likefeed(@PathVariable long id,Principal principal){
		

		try {

			Optional<User> user = userService.findUserNickname(principal.getName());

			Optional<FeedList> checkFeedLikeUser = feedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());
			if(checkFeedLikeUser.isPresent()) {
				return new ResponseEntity<>("이미 좋아요를 눌렀습니다.",HttpStatus.BAD_REQUEST);	

			}
			
			Optional<FeedList> feedList = feedListService.likefeed(user,id);
			FeedListLikeDTO feedListLike=new FeedListLikeDTO(feedList.get(),user.get());
			feedListLike.setUserKey(user.get().getId());

			alarmController.alertlike(feedList.get(),user.get());//피드 좋아요 사용자에게 알림

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
			FeedList feedList1 =new FeedList();
			FeedListLikeDTO feedListLike=new FeedListLikeDTO(feedList.get(),user.get());
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
	@ApiOperation(value="피드 신고",notes="피드 신고")
	@PostMapping("/report/feed/{id}/{reportId}")
	public ResponseEntity<?> reportFeeduser(@PathVariable long reportId,@PathVariable long id
	,Principal principal){
			Optional <User> reporter = userService.findUserNickname(principal.getName());
			Optional<FeedList> reportfeed =  feedListRepository.findById(id);
			int result1 =0,result2=0;
			if(reportfeed.isPresent()) {
				result1=reportFeedService.reportFeedSave(reportfeed.get(), reportId, reporter.get());
			}
				if (result1==0)
					return new ResponseEntity<>("피드 신고 성공",HttpStatus.OK);
				else
					return new ResponseEntity<>("이미 신고한 피드 입니다",HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value="피드 신고 취소",notes="피드 신고 취소")
	@DeleteMapping("/report/feed/{id}")
	public ResponseEntity<?> reportFeedDelete(@PathVariable long id,Principal principal){
		try {
			Optional<FeedList> reportfeed =  feedListRepository.findById(id);
			Optional <User> reporter = userService.findUserNickname(principal.getName());
			if(reportfeed.isPresent()) {
				reportFeedService.reportFeedDelete(reportfeed.get(),reporter.get());

			}
			return new ResponseEntity<>("피드 신고 삭제 성공",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="피드 신고 조회",notes="피드 신고 조회")
	@GetMapping("/report/feed")
	public ResponseEntity<?> reportFeedView(Pageable pageable){
		try {
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
			Page<ReportFeed> reportFeedPage =reportFeedService.reportfeedlist(pageable);
			return new ResponseEntity<>(reportFeedPage,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="내 피드 날짜 별 조회",notes="내 피드 날짜 별 조회")
	@GetMapping("/feed/my/date/{dates}")
	public ResponseEntity<?> mylistfeedByDate(Pageable pageable,Principal principal,@PathVariable("dates")String dates){
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
			pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다

			dates+=" 00:00:00";
			LocalDateTime startdate=LocalDateTime.parse(dates, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//시작시간
			dates=dates.replaceAll(" 00:00:00"," 23:59:59");//끝시간 설정
			LocalDateTime enddate=LocalDateTime.parse(dates, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//끝시간
			Page<FeedList> feedlist = feedListService.mylistfeedBydate(pageable,user.get().getId(),startdate,enddate);
			return new ResponseEntity<>(feedlist.stream().map(FeedListDTO::new),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="피드 날짜별 최신 한개 조회",notes="피드 날짜별 최신 한개 조회")
	@GetMapping("/feed/One/{dates}")
	public ResponseEntity<?> listfeedByDate(@PathVariable("dates")String dates,Principal principal){

			Optional<User> user = userService.findUserNickname(principal.getName());
			dates+=" 00:00:00";
			LocalDateTime startdate=LocalDateTime.parse(dates, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//시작시간
			dates=dates.replaceAll(" 00:00:00"," 23:59:59");//끝시간 설정
			LocalDateTime enddate=LocalDateTime.parse(dates, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//끝시간

			Optional <FeedList> feedlist = feedListService.mylistfeedBydateOne(user.get().getId(),startdate,enddate);
			return new ResponseEntity<>(new FeedListDTO(feedlist.get(),user.get()),HttpStatus.OK);
			//return new ResponseEntity<>(feedlist.stream().map(t->new FeedListDTO(t,user.get())),HttpStatus.OK);

	}




	@ApiOperation(value="방어로직 확인",notes="방어로직 확인")
	@GetMapping("/xss")
	public ResponseEntity<?> testXss(){
		String dirty = "<script>alert('xss');</script>";
		return new ResponseEntity<>(dirty,HttpStatus.OK);
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
