package study.share.com.source.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import study.share.com.source.message.request.TodoListReq;
import study.share.com.source.model.Color;
import study.share.com.source.model.Subject;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;
import study.share.com.source.repository.ColorRepository;
import study.share.com.source.repository.SubjectRepository;
import study.share.com.source.repository.TodoListRepository;
import study.share.com.source.repository.UserRepository;

@Service
public class TodoListService {

	@Autowired
	TodoListRepository todoListRepository;
	@Autowired
	ColorRepository colorRepository;
	@Autowired
	SubjectRepository subjectRepository;
	@Autowired
	UserRepository userRepository;

	public TodoList addtodo(TodoListReq todoListreq, Optional<User> user) {
		
		
		Color color = colorRepository.findByColor(todoListreq.getColor());
		
		Subject subject = subjectRepository.findByNameAndColor(todoListreq.getName(),color);
		TodoList todoList = new TodoList();
		if(subject==null) {//과목이 없을경우
			Subject newSubject =new Subject();
			newSubject.setName(todoListreq.getName());
			newSubject.setColor(color);
			subjectRepository.save(newSubject);
			todoList.setSubject(newSubject);
		}else {
			todoList.setSubject(subject);//과목이 널인지 아닌지에 따라 넣는게 달라짐
		}
		String[] rDate = todoListreq.getSavedDate().split(" ");
		String savedDate =rDate[0];
		todoList.setChecked(false);
		todoList.setSavedDate(savedDate);
		todoList.setTodoContent(todoListreq.getTodoContent());
		todoList.setHighlighter(todoListreq.getColor());
		todoList.setUser(user.get());
		
		
		TodoList todos=todoListRepository.save(todoList);
		return todos;
		
	}

	public List<TodoList> listtodo(String savedDate,Optional<User> user) {
		List<TodoList> listtodos = todoListRepository.findBySavedDateAndUserId(savedDate,user.get().getId());
		return listtodos;
	}

	public void deletetodo(long id) {
		todoListRepository.deleteById(id);
	}

	public List<Color> selectColorList() {
		return colorRepository.findAll();
	}

	public List<TodoList> selectMyTodoList(String today, User user) {
		String[] rDate = today.split(" ");
		String savedDate =rDate[0];
		return todoListRepository.findBySavedDateAndUser(savedDate,user);
	}

	public TodoList updateTodoCheck(long todoId) {
		
		Optional<TodoList> todolist = todoListRepository.findById(todoId);
		todolist.ifPresent(todo->{
			if(todo.isChecked()) {
				todo.setChecked(false);
			}else {
				todo.setChecked(true);
			}
			todoListRepository.save(todo);
		});
		return todolist.get();
	}

	public List<TodoList> selectUserTodoList() {
		return todoListRepository.findAllByOrderByIdDesc();
	}

	public long countComplete(String today, Optional<User> user) {
		String[] rDate = today.split(" ");
		String savedDate =rDate[0];
		return todoListRepository.countBySavedDateAndUserAndChecked(savedDate,user,true);
	}

	public long uncountComplete(String today, Optional<User> user) {
		String[] rDate = today.split(" ");
		String savedDate =rDate[0];
		return todoListRepository.countBySavedDateAndUserAndChecked(savedDate,user,false);
	}
 
}
