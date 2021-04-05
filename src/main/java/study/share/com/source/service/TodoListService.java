package study.share.com.source.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.share.com.source.message.request.TodoCommentReq;
import study.share.com.source.message.request.TodoListReq;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.TodoComment;
import study.share.com.source.model.TodoDate;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;
import study.share.com.source.repository.TodoCommentRepository;
import study.share.com.source.repository.TodoDateRepository;
import study.share.com.source.repository.TodoListRepository;
import study.share.com.source.repository.UserRepository;

@Service
public class TodoListService {

	@Autowired
	TodoListRepository todoListRepository;
	@Autowired
	TodoDateRepository todoDateRepository;
	@Autowired
	UserRepository userRepository;

	public TodoList saveTodoList(TodoDate todoDate, TodoListReq todoListreq) {
		
		boolean dateCheck = todoDateRepository.existsBySavedDateAndUser(todoDate.getSavedDate(),todoDate.getUser());
		if(!dateCheck) {
			TodoDate returnTodoDate = todoDateRepository.save(todoDate);
			TodoList todoList = new TodoList();
			todoList.setTodoDate(returnTodoDate);
			todoList.setChecked(todoListreq.getChecked());
			todoList.setUser(todoDate.getUser());
			todoList.setTodoTitle(todoListreq.getTodoTitle());
			todoList.setTodoContent(todoListreq.getTodoContent());
			todoList.setStartTime(todoListreq.getStartTime());
			todoList.setEndTime(todoListreq.getEndTime());
			 
			return todoListRepository.save(todoList);
		}else {
			TodoDate returnTodoDate = todoDateRepository.findBySavedDateAndUser(todoDate.getSavedDate(),todoDate.getUser());
			TodoList todoList = new TodoList();
			todoList.setTodoDate(returnTodoDate);
			todoList.setChecked(todoListreq.getChecked());
			todoList.setUser(todoDate.getUser());
			todoList.setTodoTitle(todoListreq.getTodoTitle());
			todoList.setTodoContent(todoListreq.getTodoContent());
			todoList.setStartTime(todoListreq.getStartTime());
			todoList.setEndTime(todoListreq.getEndTime());
			
			return todoListRepository.save(todoList);		
		}
			
	}

	public TodoDate selectMyTodoList(String savedDate, User user) {
		return todoDateRepository.findBySavedDateAndUser(savedDate,user);
	}

	public Optional<TodoList> updateMyTodoList(long id, TodoListReq todoListReq) {
		Optional<TodoList> todoList=todoListRepository.findById(id);
		todoList.ifPresent(selectList -> {
			selectList.setStartTime(todoListReq.getStartTime());
			selectList.setEndTime(todoListReq.getEndTime());
			selectList.setTodoContent(todoListReq.getTodoContent());
			selectList.setTodoTitle(todoListReq.getTodoTitle());
			selectList.setChecked(todoListReq.getChecked());
			
			todoListRepository.save(selectList);
		});
		return todoListRepository.findById(id);
	}

	public void deleteTodoList(long id) {
		todoListRepository.deleteById(id); 
	}


//	public TodoList addtodo(TodoListReq todoListreq, Optional<User> user) {
//		
//		
//		Color color = colorRepository.findByColor(todoListreq.getColor());
//		
//		Subject subject = subjectRepository.findByNameAndColor(todoListreq.getName(),color);
//		TodoList todoList = new TodoList();
//		if(subject==null) {//과목이 없을경우
//			Subject newSubject =new Subject();
//			newSubject.setName(todoListreq.getName());
//			newSubject.setColor(color);
//			subjectRepository.save(newSubject);
//			todoList.setSubject(newSubject);
//		}else {
//			todoList.setSubject(subject);//과목이 널인지 아닌지에 따라 넣는게 달라짐
//		}
//		String[] rDate = todoListreq.getSavedDate().split(" ");
//		String savedDate =rDate[0];
//		todoList.setChecked(false);
//		todoList.setSavedDate(savedDate);
//		todoList.setTodoContent(todoListreq.getTodoContent());
//		todoList.setHighlighter(todoListreq.getColor());
//		todoList.setUser(user.get());
//		
//		
//		TodoList todos=todoListRepository.save(todoList);
//		return todos;
//		
//	}
//
//	public List<TodoList> listtodo(String savedDate,Optional<User> user) {
//		List<TodoList> listtodos = todoListRepository.findBySavedDateAndUserId(savedDate,user.get().getId());
//		return listtodos;
//	}
//
//	public void deletetodo(long id) {
//		todoListRepository.deleteById(id);
//	}
//
//	public List<Color> selectColorList() {
//		return colorRepository.findAll();
//	}
//
//	public List<TodoList> selectMyTodoList(String today, User user) {
//		String[] rDate = today.split(" ");
//		String savedDate =rDate[0];
//		return todoListRepository.findBySavedDateAndUser(savedDate,user);
//	}
//
//	public TodoList updateTodoCheck(long todoId) {
//		
//		Optional<TodoList> todolist = todoListRepository.findById(todoId);
//		todolist.ifPresent(todo->{
//			if(todo.isChecked()) {
//				todo.setChecked(false);
//			}else {
//				todo.setChecked(true);
//			}
//			todoListRepository.save(todo);
//		});
//		return todolist.get();
//	}
//
//	public List<TodoList> selectUserTodoList() {
//		return todoListRepository.findAllByOrderByIdDesc();
//	}
//
//	public long countComplete(String today, Optional<User> user) {
//		String[] rDate = today.split(" ");
//		String savedDate =rDate[0];
//		return todoListRepository.countBySavedDateAndUserAndChecked(savedDate,user,true);
//	}
//
//	public long uncountComplete(String today, Optional<User> user) {
//		String[] rDate = today.split(" ");
//		String savedDate =rDate[0];
//		return todoListRepository.countBySavedDateAndUser(savedDate,user);
//	}
 
}
