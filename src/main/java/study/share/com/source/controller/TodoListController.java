package study.share.com.source.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import study.share.com.source.message.request.TodoListReq;
import study.share.com.source.model.Color;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.TodoListDTO;
import study.share.com.source.service.TodoListService;
import study.share.com.source.service.UserService;

@RestController
public class TodoListController {

	@Autowired
	UserService userService;	
	@Autowired
	TodoListService todoListService;
	
	@GetMapping(path="/todo/color")
	public ResponseEntity<?> todocolor() {
		try {
			List<Color> todoColor = todoListService.selectColorList();
			return new ResponseEntity<>(todoColor,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@PatchMapping(path="/todo/updateCheck/{todoId}")
	public ResponseEntity<?> updateCheck(@PathVariable long todoId){
		try {
			TodoList todoList=todoListService.updateTodoCheck(todoId);
			return new ResponseEntity<>(todoList,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	} 
	
	/*
	 * user의 투두리스트 조회 
	 * */
	@GetMapping(path="/todo/todofeedlist")
	public ResponseEntity<?> todofeedlist(){
		TodoList todos = new TodoList();
		List<TodoList> todoFeedList=todoListService.selectUserTodoList();
		List<Map<User, List<TodoList>>> todofeed=todoFeedList
											.stream()  
											.collect(Collectors.groupingBy(TodoList::getSavedDate,
													Collectors.groupingBy(TodoList::getUser)))
											.values().stream()
											.collect(Collectors.toList())
											;
		return new ResponseEntity<>(todofeed,HttpStatus.OK);
	}
	/*
	 * user의 투두리스트 조회 (개인)
	 * */
	@GetMapping(path="/todo/mytodolist/{today}")
	public ResponseEntity<?> mytodolist(@PathVariable String today,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			List<TodoList> todolist = todoListService.selectMyTodoList(today,user.get());

			return new ResponseEntity<>(todolist.stream().map(TodoListDTO::new),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	/*
	 * user의 투두리스트 진행량 조회 (개인)
	 * */
	@GetMapping(path="/todo/mytodolistcount/{today}")
	public ResponseEntity<?> mytodolistcount(@PathVariable String today,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			long completeTodo= todoListService.countComplete(today,user);
			long totalcompleteTodo= todoListService.uncountComplete(today,user);
			System.out.println("completeTodo=="+completeTodo);
			System.out.println("totalcompleteTodo=="+totalcompleteTodo);
			long perCompleteTodo = Math.round(((double)completeTodo/(double)totalcompleteTodo)*100);
			System.out.println("totalcompleteTodo=="+perCompleteTodo);
			
			return new ResponseEntity<>(new TodoListDTO(completeTodo,totalcompleteTodo,perCompleteTodo),HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	
	
	@PostMapping(path="/user/todo")
	public ResponseEntity<?> addtodo(@RequestBody TodoListReq todoListreq,Principal principal) {

	
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			TodoList todoList =todoListService.addtodo(todoListreq,user);
			System.out.println(todoList.toString());
			return new ResponseEntity<>(todoList,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	@PatchMapping(path="/user/todo")//임시로 patch로 함 
	public ResponseEntity<?> listtodo(@RequestBody Map<String, String> data,Principal principal) {
		
		//String savedDate = data.get("savedDate");
		String savedDate=data.get("savedDate");
		Optional<User> user = userService.findUserNickname(principal.getName());
		List<TodoList> todoList=todoListService.listtodo(savedDate,user);
		return new ResponseEntity<>(todoList,HttpStatus.OK);
	}
	@DeleteMapping(path="/todo/deleteTodo/{todoId}")
	public ResponseEntity<?> deletetodo(@PathVariable long todoId,Principal principal){
		
		try {
			todoListService.deletetodo(todoId);
			return new ResponseEntity<>(todoId,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
		
	}
}
