package study.share.com.source.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;

public interface FeedReplyRepository extends JpaRepository<FeedReply,Long>{

	List<FeedReply> findByFeedlist_id(long id, Pageable pageable);

}
