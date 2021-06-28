package study.share.com.source.repository.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.todo.TodoComment;
import study.share.com.source.model.todo.TodoDate;
import study.share.com.source.model.User;


public interface TodoCommentRepository extends JpaRepository<TodoComment,Long> {

	boolean existsByTodoDate(TodoDate returnTodoDate);

	TodoComment findAllByTodoDateSavedDateAndUser(String savedDate, User user);
	


//  @Query("UPDATE auth_token SET accessToken = accessToken WHERE id=(:id)")
//  void updateAuthTokfnInfo(Long id, String accessToken, String refreshToken, int accessExpiresIn, int refreshExpiresIn);
}
