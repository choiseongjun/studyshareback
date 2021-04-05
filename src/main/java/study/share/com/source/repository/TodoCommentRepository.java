package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.TodoComment;
import study.share.com.source.model.TodoDate;
import study.share.com.source.model.User;


public interface TodoCommentRepository extends JpaRepository<TodoComment,Long> {

	boolean existsByTodoDate(TodoDate returnTodoDate);
	


//  @Query("UPDATE auth_token SET accessToken = accessToken WHERE id=(:id)")
//  void updateAuthTokfnInfo(Long id, String accessToken, String refreshToken, int accessExpiresIn, int refreshExpiresIn);
}
