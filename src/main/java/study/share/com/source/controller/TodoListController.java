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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping(path="/user/todo")
	public ResponseEntity<?> addtodo(@RequestBody TodoList todoList,Principal principal) {
		
		Optional<User> user = userService.findUserNickname(principal.getName());
		
		todoListService.addtodo(todoList,user);
		
		return null;
	}
	@PatchMapping(path="/user/todo")//임시로 patch로 함 
	public ResponseEntity<?> listtodo(@RequestBody Map<String, String> data,Principal principal) {
		
		//String savedDate = data.get("savedDate");
		System.out.println("saveddate=="+data.get("savedDate"));
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
