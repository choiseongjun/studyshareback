package study.share.com.source.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import study.share.com.source.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByNickname(String nickname);
	boolean existsByUserid(String email);
	Optional<User> findByUserid(String username);
	Optional<User> findByNickname(String username);
	Optional<User> findByEmail(String email);

	@Query("SELECT IFNULL(MAX(id),0)+1 FROM User")
	long selectusermaxid();
	
	List<User> findByNicknameLike(String string);
	boolean existsByEmail(String email);
	boolean existsByFcmToken(String fcmToken);
	Optional<User> findByFcmToken(String fcmToken);

}