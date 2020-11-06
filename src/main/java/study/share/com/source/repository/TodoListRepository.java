package study.share.com.source.repository;

import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList,Long>{

	List<TodoList> findAllBySavedDate(String savedDate);
	
	List<TodoList> findBySavedDateAndUserId(String savedDate, Long id);

	List<TodoList> findBySavedDateAndUser(String today, User user);
}
