package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.FeedReplyLike;

import java.util.Optional;

public interface FeedReplyLikeRepository extends JpaRepository<FeedReplyLike,Long> {

    Optional<FeedReplyLike> findByuser_idAndId(long user_id, long id);

}
