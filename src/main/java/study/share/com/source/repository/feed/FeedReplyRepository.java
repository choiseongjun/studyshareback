package study.share.com.source.repository.feed;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.feed.FeedReply;

public interface FeedReplyRepository extends JpaRepository<FeedReply,Long>{

	List<FeedReply> findByFeedlist_id(long id, Pageable pageable);

	//Page<FeedReply> findByFeedlist_idAndDeleteyn(long id, Pageable pageable, char c);

	Optional<FeedReply> findById (long id);

	@Modifying
	@Transactional
	@Query("UPDATE FeedReply f set f.groupOrd=f.groupOrd+1 WHERE f.originNo=:originNo AND f.groupOrd>:groupOrd")
	int updateorder(@Param(value = "originNo") long originNo,@Param(value = "groupOrd") long groupOrd);

	Page<FeedReply> findByFeedlist_idAndDeleteyn(Pageable pageable, long id, char c);

	FeedReply findByOriginNoAndGroupOrd(long id, long l);

	Page<FeedReply> findByFeedlist_idAndDeleteynAndGroupOrd(Pageable pageable, long id, char c, long l);

	Page<FeedReply> findByOriginNoAndDeleteyn(Pageable pageable, Long id, char c);

	Page<FeedReply> findByOriginNoAndDeleteynOrderByGroupOrdDesc(Pageable pageable, Long id, char c);

	List<FeedReply> findByIdAndFeedReplylikeUserId(Long id, Long id2);

	Page<FeedReply> findByDeleteynOrFeedReplylikeUserIdOrderByGroupOrdDesc(Pageable pageable, char c, Long id);

	Page<FeedReply> findByDeleteynOrFeedReplylikeUserIdOrFeedReplylikeUserIdIsNullOrderByGroupOrdDesc(Pageable pageable,
			char c, Long id);

	Page<FeedReply> findByDeleteynAndFeedReplylikeUserIdOrderByGroupOrdDesc(Pageable pageable, char c, Long id);

	Page<FeedReply> findByFeedlist_idAndDeleteynOrFeedReplylikeUserIdAndFeedReplylikeUserIdIsNullAndFeedReplylikeUserIdIsNotNullOrderByGroupOrdDesc(
			Pageable pageable, Long id, char c, Long id2);
}
