package study.share.com.source.repository.feed;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.share.com.source.model.feed.FeedLike;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.User;

public interface FeedLikeRepository extends JpaRepository<FeedLike,Long>{

	@Query("SELECT id FROM FeedLike WHERE feedlist=(:feedlist) AND user=(:user) ")
	long findlikeno(@Param("user") User user,@Param("feedlist") FeedList feedlist);

	Page<FeedLike> findByFeedlistId(long id, Pageable pageable);


}
