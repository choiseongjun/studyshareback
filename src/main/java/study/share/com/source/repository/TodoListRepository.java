package study.share.com.source.repository;

import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository<TodoList,Long>{

//	List<TodoList> findAllBySavedDate(String savedDate);
//	
//	List<TodoList> findBySavedDateAndUserId(String savedDate, Long id);
//
//	List<TodoList> findBySavedDateAndUser(String today, User user);
//
//	List<TodoList> findAllByOrderByIdDesc();
//
//	long countBySavedDateAndUser(String today, Optional<User> user);
//
//	long countBySavedDateAndUserAndChecked(String today, Optional<User> user, boolean b);

}
