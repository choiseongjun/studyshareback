package study.share.com.source.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.Follow;
import study.share.com.source.model.User;

public interface FollowRepository extends JpaRepository<Follow, Long>{

	void deleteByFromUserIdAndToUserId(User user, User user2);

	void deleteByFromUserIdAndToUserId(Long id, Long id2);

	Page<Follow> findAllByToUserId(Long id, Pageable pageable);

	List<Follow> findByFromUserId(Long id);

	List<Follow> findByToUserId(Long id);

	Page<Follow> findAllByFromUserId(Long id,Pageable pageable);

	Optional<Follow> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

}
