package study.share.com.source.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.share.com.source.model.Color;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;
import study.share.com.source.repository.ColorRepository;
import study.share.com.source.repository.TodoListRepository;

@Service
public class TodoListService {

	@Autowired
	TodoListRepository todoListRepository;
	@Autowired
	ColorRepository colorRepository;

	public void addtodo(TodoList todoList, Optional<User> user) {
		
		todoList.setUser(user.get());
		
		todoListRepository.save(todoList);
		
	}

	public List<TodoList> listtodo(String savedDate,Optional<User> user) {
		List<TodoList> listtodos = todoListRepository.findBySavedDateAndUserId(savedDate,user.get().getId());
		return listtodos;
	}

	public void deletetodo(long id) {
		todoListRepository.deleteById(id);;
	}

	public List<Color> selectColorList() {
		return colorRepository.findAll();
	}
}
