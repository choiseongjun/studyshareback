package study.share.com.source.repository.todo;

import study.share.com.source.model.todo.TodoList;
import study.share.com.source.model.User;

import java.time.LocalDateTime;
import java.util.List;

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

    long countByCheckedAndUser_id(char checked,long user_id);

    long countByuser_id(long user_id);

	List<TodoList> findAllByTodoListsSavedDateAndUser(String savedDate, User user);

	List<TodoList> findAllByuserIdAndUpdatedAtBetween(long userId, LocalDateTime startdate, LocalDateTime enddate);

    List<TodoList> findAllByuserIdAndTodoListsSavedDateContaining(long userId, String saveDate);
}
