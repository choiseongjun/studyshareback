package study.share.com.source.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.xml.bind.v2.TODO;

import study.share.com.source.message.request.TodoListReq;
import study.share.com.source.model.Color;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;
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
	@GetMapping(path="/todo/mytodolist/{today}")
	public ResponseEntity<?> mytodolist(@PathVariable String today,Principal principal){
		
		try {
			Optional<User> user = userService.findUserNickname(principal.getName());
			List<TodoList> todolist = todoListService.selectMyTodoList(today,user.get());
			return new ResponseEntity<>(todolist,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
	}
	
	
	@PostMapping(path="/user/todo")
	public ResponseEntity<?> addtodo(@RequestBody TodoListReq todoListreq,Principal principal) {

		System.out.println(todoListreq.toString());
		
	
		Optional<User> user = userService.findUserNickname(principal.getName());
//		
		TodoList todoList =todoListService.addtodo(todoListreq,user);
		
		return null;
	}
	@PatchMapping(path="/user/todo")//임시로 patch로 함 
	public ResponseEntity<?> listtodo(@RequestBody Map<String, String> data,Principal principal) {
		
		//String savedDate = data.get("savedDate");
		String savedDate=data.get("savedDate");
		Optional<User> user = userService.findUserNickname(principal.getName());
		List<TodoList> todoList=todoListService.listtodo(savedDate,user);
		return new ResponseEntity<>(todoList,HttpStatus.OK);
	}
	@GetMapping(path="/user/todo/{id}")
	public ResponseEntity<?> deletetodo(@PathVariable long id,Principal principal){
		
		try {
			todoListService.deletetodo(id);
			return new ResponseEntity<>("",HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);	
		}
		
	}
}
