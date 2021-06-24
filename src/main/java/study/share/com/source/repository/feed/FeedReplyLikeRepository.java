package study.share.com.source.repository.feed;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.feed.FeedReplyLike;

import java.util.Optional;

public interface FeedReplyLikeRepository extends JpaRepository<FeedReplyLike,Long> {

	Optional<FeedReplyLike> findByIdAndUserId(long id, Long id2);

	Optional<FeedReplyLike> findByFeedReplyIdAndUserId(long id, Long id2);

	void deleteByFeedReplyId(long id);

	void deleteByFeedReplyIdAndUserId(long id, Long id2);

}
