package study.share.com.source.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

import io.swagger.annotations.ApiOperation;
import study.share.com.source.message.request.TodoCommentReq;
import study.share.com.source.message.request.TodoListReq;
import study.share.com.source.message.response.TodoDateResponse;
import study.share.com.source.model.todo.TodoComment;
import study.share.com.source.model.todo.TodoDate;
import study.share.com.source.model.todo.TodoList;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.TodoListDTO;
import study.share.com.source.repository.todo.TodoCommentRepository;
import study.share.com.source.repository.todo.TodoDateRepository;
import study.share.com.source.service.*;
import study.share.com.source.service.feed.FeedListService;
import study.share.com.source.service.todo.TodoCommentService;
import study.share.com.source.service.todo.TodoDateService;
import study.share.com.source.service.todo.TodoListService;

@RestController
public class TodoListController {

	@Autowired
	UserService userService;	
	@Autowired
	TodoListService todoListService;
	@Autowired
	TodoDateService todoDateService;
	@Autowired
	TodoCommentService todoCommentService;
	@Autowired
	TodoDateRepository todoDateRepository;
	@Autowired
	TodoCommentRepository todoCommentRepository;
	@Autowired
    FeedListService feedListService;
	
	@ApiOperation(value="내 투두리스트 작성",notes="내 투두리스트 작성")
	@PostMapping(path="/user/todo")
	public ResponseEntity<?> addtodo(@RequestBody TodoListReq todoListreq,Principal principal) {

	
		try {
			if(principal==null) { 
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
			}  
			Optional<User> user = userService.findUserNickname(principal.getName());
			TodoDate todoDate =new TodoDate();
			todoDate.setSavedDate(todoListreq.getSavedDate());
			//todoDate.setUser(user.get());
			
			TodoList todoList =todoListService.saveTodoList(user.get(),todoDate,todoListreq);
			long Completeresult= todoListService.getMyAchievement(user.get().getId());
			long resultAll =todoListService.getAllPlan(user.get().getId());
			return new ResponseEntity<>(new TodoListDTO(todoList, user.get(),Completeresult,resultAll),HttpStatus.OK);	
			//return new ResponseEntity<>(new TodoListDTO(todoList, user.get(),Completeresult,resultAll),HttpStatus.OK);	

		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@ApiOperation(value="내 투두리스트 커멘츠 작성",notes="내 투두리스트 커멘츠 작성")
	@PostMapping(path="/user/todoComment")
	public ResponseEntity<?> addtodoComment(@RequestBody TodoCommentReq todoCommentReq,Principal principal) {
	
		if(principal==null) { 
			return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
		}
		Optional<User> user = userService.findUserNickname(principal.getName());
		
		TodoDate todoDate =new TodoDate();
		todoDate.setSavedDate(todoCommentReq.getSavedDate());
		//todoDate.setUser(user.get());

		TodoComment todoCmt =new TodoComment();
		todoCmt.setTodoDate(todoDate);
		TodoDate returnTodoDate=todoDateRepository.findBySavedDate(todoCommentReq.getSavedDate());

		if(returnTodoDate==null) {

			TodoComment todoComment=todoCommentService.saveComment(user.get(),todoDate,todoCommentReq);
			return new ResponseEntity<>(todoComment,HttpStatus.OK);
		}else {
			TodoComment todoComment=todoCommentService.saveComment(user.get(),returnTodoDate,todoCommentReq);
			return new ResponseEntity<>(todoComment,HttpStatus.OK);
		}
	}
	/*
	 * user의 투두리스트 조회 (개인)
	 * */
	@ApiOperation(value="내 투두리스트 조회",notes="내 투두리스트 조회")
	@GetMapping(path="/todo/mytodolist/{savedDate}")
	public ResponseEntity<?> mytodolist(@PathVariable String savedDate,Principal principal){
		
		try {
			if(principal==null) { 
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
			}
			Optional<User> user = userService.findUserNickname(principal.getName());
			TodoDate tododate = todoListService.selectMyTodoList(savedDate,user.get()); //tododate로 조회 
			List<TodoList> todoList= todoListService.MyTodoList(savedDate,user.get()); //todolist로 조회
			TodoComment todoComment = todoListService.MyTodoComment(savedDate,user.get());
			if(tododate!=null ) {
				return new ResponseEntity<>(new TodoDateResponse(tododate,todoList,todoComment),HttpStatus.OK);	
			}else { 
				TodoDate emptyTododate=new TodoDate();
				List<TodoList> emptyTodoList = new ArrayList<TodoList>();
				TodoComment emptyTodoComment = new TodoComment();
				return new ResponseEntity<>(new TodoDateResponse(emptyTododate,emptyTodoList,emptyTodoComment),HttpStatus.OK);	
			}
			//return new ResponseEntity<>("성공.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@ApiOperation(value="내 투두리스트 수정",notes="내 투두리스트 수정")
	@PatchMapping(path="/user/todo/{id}") 
	public ResponseEntity<?> updateTodolist(@PathVariable long id,@RequestBody TodoListReq todoListReq,Principal principal){
		try { 
			if(principal==null) { 
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
			}
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<TodoList> todolist = todoListService.updateMyTodoList(id,todoListReq); 
			
			return new ResponseEntity<>(todolist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	@ApiOperation(value="내 투두리스트 코맨트 수정",notes="내 투두리스트 코맨트  수정")
	@PatchMapping(path="/user/todoComment/{id}") 
	public ResponseEntity<?> updateTodoComment(@PathVariable long id,@RequestBody TodoCommentReq todoCommentReq,Principal principal){
		
		try { 
			if(principal==null) { 
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
			}
			Optional<User> user = userService.findUserNickname(principal.getName());
			Optional<TodoComment> todoComment = todoCommentService.updateMyTodoComment(id,todoCommentReq);
 
			return new ResponseEntity<>(todoComment,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}
	@ApiOperation(value="내 투두리스트 코맨트 삭제",notes="내 투두리스트 코맨트  삭제")
	@DeleteMapping(path="/user/todoComment/{id}") 
	public ResponseEntity<?> deleteTodoComment(@PathVariable long id,Principal principal){
			
			try { 
				if(principal==null) { 
					return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
				}
				todoCommentService.deleteTodoComment(id);
	 
				return new ResponseEntity<>(id,HttpStatus.OK);
			}catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
			}
	}
	@ApiOperation(value="내 투두리스트 삭제",notes="내 투두리스트  삭제")
	@DeleteMapping(path="/user/todo/{id}") 
	public ResponseEntity<?> deleteTodoList(@PathVariable long id,Principal principal){
		try { 
			if(principal==null) {  
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);	
			}
			todoListService.deleteTodoList(id);
   
			return new ResponseEntity<>(id,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		} 
	}

	@ApiOperation(value="내 투두리스트 달성률 조회",notes="내 투두리스트 달성률 조회")
	@GetMapping(path="/todo/mytodolist/achievement")
	public ResponseEntity<?> mytodolistacheive(Principal principal){

		try {
			Map<String, Long> map =new HashMap<String, Long>();
			if(principal==null) {
				return new ResponseEntity<>("로그인을 해주세요",HttpStatus.FORBIDDEN);
			}
			Optional<User> user = userService.findUserNickname(principal.getName());
			long Completeresult= todoListService.getMyAchievement(user.get().getId());
			long resultAll =todoListService.getAllPlan(user.get().getId());

			Double result =(Completeresult/(double)resultAll);
			
			map.put("completeRatioCnt", Completeresult);
			map.put("allRatioCnt",resultAll);

//			return new ResponseEntity<>(String.format("%.2f",result),HttpStatus.OK);
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value="투두리스트 월별 달성률 조회",notes="투두리스트 월별 달성률 조회")
	@GetMapping("/todo/{user_id}/monthly/achievement/{dates}")
	public ResponseEntity<?> feedlistByDate(@PathVariable("user_id")long user_id,@PathVariable("dates")String dates){

		Optional<User> user = userService.findUserId(user_id);
		if(!user.isPresent())
			return new ResponseEntity<>("해당 사용자가 존재하지 않습니다",HttpStatus.BAD_REQUEST);

		List<TodoList> todolist = todoListService.mylistfeedBydate(user.get().getId(),dates);
//		System.out.println("------시작 투두");
//		for(int i=0; i<todolist.size(); i++)
//			System.out.println("날짜: "+todolist.get(i).getTodoLists().getSavedDate()+" 그리고 여부: "+todolist.get(i).getChecked());
//		System.out.println("---------");
		HashMap<String,Integer> result = new HashMap<String,Integer>();//날짜, 글의 갯수
		HashMap<String,Integer> temp = new HashMap<String,Integer>();//날짜, 달성 확인
		HashMap<String,Double> value = new HashMap<String,Double>();//최종 결과
		ArrayList <String> saveDate = new ArrayList<>();
		ArrayList <Character> saveCheck = new ArrayList<>();


		for(int i=0 ; i<todolist.size(); i++)//시간 단위 지우기
		{
			saveDate.add(todolist.get(i).getTodoLists().getSavedDate());//string 으로 변환 하여 저장
			saveCheck.add(todolist.get(i).getChecked());//달성 여부 저장
		}
		//temp에 default 값 0으로 설정
		for(int i=0; i<saveDate.size();i++)
			temp.put(saveDate.get(i),0);

		//날짜별 글 수 세기 (일별 전체 투두리스트 수 세기)
		for(int i=0; i<saveDate.size();i++)
		{
			if(result.containsKey(saveDate.get(i)))//key가 존재하는 경우
			{
				result.put(saveDate.get(i),result.get(saveDate.get(i))+1);
				if(saveCheck.get(i)=='C')//달성한 목표이면 갯수 세기
					temp.put(saveDate.get(i),temp.get(saveDate.get(i))+1);
			}
			else//한개만 존재하는 경우
			{
				result.put(saveDate.get(i),1);
				if(saveCheck.get(i)=='C')//한개가 달성한 목표이면 갯수 세기
					temp.put(saveDate.get(i),1);
			}
		}

//		for(String date : result.keySet())
//		{
//			System.out.println("날짜 "+date);
//			System.out.println("총 갯수 출력 "+result.get(date));
//			System.out.println("총 달성 갯수 "+temp.get(date));
//			float m=((float)temp.get(date)/(float)result.get(date))*100;
//			System.out.println("결과물 "+m);
//		}

		for(String date : result.keySet())//달성률 계산
			value.put(date,Math.floor(((float)temp.get(date)/(float)result.get(date))*100));

		return new ResponseEntity<>(value,HttpStatus.OK);
	}
	/*
//	@ApiOperation(value="투두 컬러 조회",notes="투두 컬러 조회")
//	@GetMapping(path="/todo/color")
//	public ResponseEntity<?> todocolor() {
//		try {
//			List<Color> todoColor = todoListService.selectColorList();
//			return new ResponseEntity<>(todoColor,HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	}
//	@ApiOperation(value="투두리스트 체크",notes="투두리스트 체크")
//	@PatchMapping(path="/todo/updateCheck/{todoId}")
//	public ResponseEntity<?> updateCheck(@PathVariable long todoId){
//		try {
//			TodoList todoList=todoListService.updateTodoCheck(todoId);
//			return new ResponseEntity<>(todoList,HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	} 
//	
//	/*
//	 * user의 투두리스트 조회 
//	 * */
//	@ApiOperation(value="투두리스트 전체조회",notes="투두리스트 전체조회")
//	@GetMapping(path="/todo/todofeedlist")
//	public ResponseEntity<?> todofeedlist(){
//		TodoList todos = new TodoList();
//		List<TodoList> todoFeedList=todoListService.selectUserTodoList();
//		List<Map<User, List<TodoList>>> todofeed=todoFeedList
//											.stream()  
//											.collect(Collectors.groupingBy(TodoList::getSavedDate,
//													Collectors.groupingBy(TodoList::getUser)))
//											.values().stream()
//											.collect(Collectors.toList())
//											;
//		return new ResponseEntity<>(todofeed,HttpStatus.OK);
//	}
//	/*
//	 * user의 투두리스트 조회 (개인)
//	 * */
//	@ApiOperation(value="내 투두리스트 조회",notes="내 투두리스트 조회")
//	@GetMapping(path="/todo/mytodolist/{today}")
//	public ResponseEntity<?> mytodolist(@PathVariable String today,Principal principal){
//		
//		try {
//			Optional<User> user = userService.findUserNickname(principal.getName());
//			List<TodoList> todolist = todoListService.selectMyTodoList(today,user.get());
//
//			return new ResponseEntity<>(todolist.stream().map(TodoListDTO::new),HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	}
//	/*
//	 * user의 투두리스트 진행량 조회 (개인)
//	 * */
//	@ApiOperation(value="내 투두리스트 진행량 조회",notes="내 투두리스트 진행량 조회")
//	@GetMapping(path="/todo/mytodolistcount/{today}")
//	public ResponseEntity<?> mytodolistcount(@PathVariable String today,Principal principal){
//		
//		try {
//			Optional<User> user = userService.findUserNickname(principal.getName());
//			long completeTodo= todoListService.countComplete(today,user);
//			long totalcompleteTodo= todoListService.uncountComplete(today,user);
//			long perCompleteTodo = Math.round(((double)completeTodo/(double)totalcompleteTodo)*100);
//			
//			return new ResponseEntity<>(new TodoListDTO(completeTodo,totalcompleteTodo,perCompleteTodo),HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	}
//	
//	@ApiOperation(value="내 투두리스트 작성",notes="내 투두리스트 작성")
//	@PostMapping(path="/user/todo")
//	public ResponseEntity<?> addtodo(@RequestBody TodoListReq todoListreq,Principal principal) {
//
//	
//		try {
//			Optional<User> user = userService.findUserNickname(principal.getName());
//			TodoList todoList =todoListService.addtodo(todoListreq,user);
//			System.out.println(todoList.toString());
//			return new ResponseEntity<>(todoList,HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//	}
//	@ApiOperation(value="내 투두리스트 수정",notes="내 투두리스트 수정")
//	@PatchMapping(path="/user/todo")//임시로 patch로 함 
//	public ResponseEntity<?> listtodo(@RequestBody Map<String, String> data,Principal principal) {
//		
//		//String savedDate = data.get("savedDate");
//		String savedDate=data.get("savedDate");
//		Optional<User> user = userService.findUserNickname(principal.getName());
//		List<TodoList> todoList=todoListService.listtodo(savedDate,user);
//		return new ResponseEntity<>(todoList,HttpStatus.OK);
//	}
//	
//	@ApiOperation(value="내 투두리스트 삭제",notes="내 투두리스트 삭제")
//	@DeleteMapping(path="/todo/deleteTodo/{todoId}")
//	public ResponseEntity<?> deletetodo(@PathVariable long todoId,Principal principal){
//		
//		try {
//			todoListService.deletetodo(todoId);
//			return new ResponseEntity<>(todoId,HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
//		}
//		
//	}
}
