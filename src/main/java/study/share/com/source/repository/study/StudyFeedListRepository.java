package study.share.com.source.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.studygroup.StudyFeedList;
import study.share.com.source.model.studygroup.StudyFeedReply;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudyFeedListRepository extends JpaRepository<StudyFeedList,Long> {

    @Query("SELECT IFNULL(MAX(id),0)+1 FROM StudyFeedList")
    long selectmaxid();

    //long countByFeedlikeId(long id);

    //List<StudyFeedList> findAllByDeleteyn(char c);

    Page<StudyFeedList> findAllByDeleteynOrderByIdDesc(Pageable pageable, char c);
//    @Query("SELECT IFNULL(MAX(totallike),0)+1 FROM StudyFeedList")
//    long selectmaxtotalLiekid();

    //List<StudyFeedReply> findByIdAndFeedreplyDeleteyn(long id, char c);

    Page <StudyFeedList> findAllByUserId(Pageable pageable,long userId);

   // Page<StudyFeedList> findAllByDeleteynAndFeedlikeUserIdOrderByIdDesc(Pageable pageable, char c, Long id);

    Optional<StudyFeedList> findByIdAndStudyfeedlikeUserId(long id, Long id2);

   // Page<StudyFeedList> findAllByDeleteynAndFeedlikeUserIdInOrderByIdDesc(Pageable pageable, char c, Collection<Long> userId);

   // Page<StudyFeedList> findByDeleteynAndFeedlikeUserIdOrderByIdDesc(Pageable pageable, char c, Long id);

   // Page<StudyFeedList> findByIdAndFeedlikeUserId(Pageable pageable, char c, Long id);


   // Page<StudyFeedList> findAllByDeleteynAndFeedlikeUserIdOrFeedlikeUserIdIsNullOrderByIdDesc(Pageable pageable, char c,
     //                                                                                    Long id);

   // Page<StudyFeedList> findAllByDeleteynOrFeedlikeUserIdOrFeedlikeUserIdIsNullOrderByIdDesc(Pageable pageable, char c,
    //                                                                                    Long id);
//
//    Page<StudyFeedList> findDistinctAllByDeleteynOrFeedlikeUserIdOrFeedlikeUserIdIsNullOrderByIdDesc(Pageable pageable,
//                                                                                                char c, Long id);
//
//    List<StudyFeedList> findDistinctByIdOrFeedlikeUserIdOrFeedlikeUserIdIsNull(Long id, Long id2);
//
//    List<StudyFeedList> findByIdAndFeedlikeUserIdOrFeedlikeUserIdIsNull(long id, Long id2);
//
//    Page<StudyFeedList> findDistinctAllByDeleteynAndFeedlikeUserIdOrderByIdDesc(Pageable pageable, char c, Long id);
//
//    Page<StudyFeedList> findDistinctAllByDeleteynOrFeedlikeUserIdOrFeedlikeUserIdIsNullOrFeedlikeUserIdIsNotNullOrderByIdDesc(
//            Pageable pageable, char c, Long id);

    Page<StudyFeedList> findDistinctAllByUserIdNotInAndDeleteynOrStudyfeedlikeUserIdAndStudyfeedlikeUserIdIsNullAndStudyfeedlikeUserIdIsNotNullOrderByIdDesc(
            Pageable pageable,List UserId, char c,  Long id);//차단하는 유저가 있는 경우

    Page<StudyFeedList> findDistinctAllByDeleteynOrStudyfeedlikeUserIdAndStudyfeedlikeUserIdIsNullAndStudyfeedlikeUserIdIsNotNullOrderByIdDesc(
            Pageable pageable, char c,  Long id);//차단하는 유저가 없는 경우

    Page<StudyFeedList> findAllByUserIdAndDeleteyn(Pageable pageable, long user_id, char c);

    List <StudyFeedList> findAllByuserIdAndUpdatedAtBetween(long userId, LocalDateTime startdate, LocalDateTime enddate);

    Optional <StudyFeedList> findTop1ByuserIdAndUpdatedAtBetweenOrderByUpdatedAtDesc( long userId, LocalDateTime startdate, LocalDateTime enddate);

    long countByUser(User user);

    List<StudyFeedList> findByUser(User user);
}
