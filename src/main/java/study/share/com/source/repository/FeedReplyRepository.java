package study.share.com.source.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;

public interface FeedReplyRepository extends JpaRepository<FeedReply,Long>{

	List<FeedReply> findByFeedlist_id(long id, Pageable pageable);

	List<FeedReply> findByFeedlist_idAndDeleteyn(long id, Pageable pageable, char c);

	Optional<FeedReply> findById (long id);

	@Modifying
	@Transactional
	@Query("UPDATE FeedReply f set f.group_ord=f.group_ord+1 WHERE f.origin_no=:origin_no AND f.group_ord>:group_ord")
	void updateorder(long origin_no, long group_ord);
}
