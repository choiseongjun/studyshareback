package study.share.com.source.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import study.share.com.source.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByNickname(String nickname);
	boolean existsByUserid(String email);
	Optional<User> findByUserid(String username);
	Optional<User> findByNickname(String username);
}