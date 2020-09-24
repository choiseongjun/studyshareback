package study.share.com.source.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import study.share.com.source.model.FeedList;

public interface FeedListRepository extends JpaRepository<FeedList, Long>{

	@Query("SELECT IFNULL(MAX(id),0)+1 FROM FeedList")
	long selectmaxid();

	long countByFeedlikeId(long id);

	List<FeedList> findAllByDeleteyn(char c);

	List<FeedList> findAllByDeleteynOrderByIdDesc(char c);
	@Query("SELECT IFNULL(MAX(totallike),0)+1 FROM FeedList")
	long selectmaxtotalLiekid();
	

}
