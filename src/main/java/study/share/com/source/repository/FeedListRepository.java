package study.share.com.source.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;

public interface FeedListRepository extends JpaRepository<FeedList, Long>{

	@Query("SELECT IFNULL(MAX(id),0)+1 FROM FeedList")
	long selectmaxid();

	long countByFeedlikeId(long id);

	List<FeedList> findAllByDeleteyn(char c);

	Page<FeedList> findAllByDeleteynOrderByIdDesc(Pageable pageable, char c);
	@Query("SELECT IFNULL(MAX(totallike),0)+1 FROM FeedList")
	long selectmaxtotalLiekid();

	List<FeedReply> findByIdAndFeedreplyDeleteyn(long id, char c);

	Page <FeedList> findAllByuser_id(Pageable pageable,long user_id);

	Page<FeedList> findAllByDeleteynAndFeedlikeUserIdOrderByIdDesc(Pageable pageable, char c, Long id);

	Optional<FeedList> findByIdAndFeedlikeUserId(long id, Long id2);

	Page<FeedList> findAllByDeleteynAndFeedlikeUserIdInOrderByIdDesc(Pageable pageable, char c, Collection<Long> userId);

	Page<FeedList> findByDeleteynAndFeedlikeUserIdOrderByIdDesc(Pageable pageable, char c, Long id);

	Page<FeedList> findByIdAndFeedlikeUserId(Pageable pageable, char c, Long id);


	Page<FeedList> findAllByDeleteynAndFeedlikeUserIdOrFeedlikeUserIdIsNullOrderByIdDesc(Pageable pageable, char c,
			Long id);

	Page<FeedList> findAllByDeleteynOrFeedlikeUserIdOrFeedlikeUserIdIsNullOrderByIdDesc(Pageable pageable, char c,
			Long id);

	Page<FeedList> findDistinctAllByDeleteynOrFeedlikeUserIdOrFeedlikeUserIdIsNullOrderByIdDesc(Pageable pageable,
			char c, Long id);


}
