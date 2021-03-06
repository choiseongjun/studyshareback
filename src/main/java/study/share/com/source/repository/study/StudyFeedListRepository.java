package study.share.com.source.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.share.com.source.model.study.StudyFeedList;

import java.util.Optional;

public interface StudyFeedListRepository extends JpaRepository<StudyFeedList,Long> {

    @Query("SELECT IFNULL(MAX(id),0)+1 FROM StudyFeedList")
    long selectmaxid();
//
//    //long countByFeedlikeId(long id);
//
//    //List<StudyFeedList> findAllByDeleteyn(char c);
//
    Page<StudyFeedList> findAllByDeleteynOrderByIdDesc(Pageable pageable, char c);

////    @Query("SELECT IFNULL(MAX(totallike),0)+1 FROM StudyFeedList")
////    long selectmaxtotalLiekid();
//
//    //List<StudyFeedReply> findByIdAndFeedreplyDeleteyn(long id, char c);
//
    Page <StudyFeedList> findAllByUserId(Pageable pageable,long userId);
//
//   // Page<StudyFeedList> findAllByDeleteynAndFeedlikeUserIdOrderByIdDesc(Pageable pageable, char c, Long id);
//
    Optional<StudyFeedList> findByIdAndStudyfeedlikeUserId(long id, Long id2);
//
//
////    Page<StudyFeedList> findDistinctAllByUserIdNotInAndDeleteynOrStudyfeedlikeUserIdAndStudyfeedlikeUserIdIsNullAndStudyfeedlikeUserIdIsNotNullOrderByIdDesc(
////            Pageable pageable,List UserId, char c,  Long id);//차단하는 유저가 있는 경우
////
////    Page<StudyFeedList> findDistinctAllByDeleteynOrStudyfeedlikeUserIdAndStudyfeedlikeUserIdIsNullAndStudyfeedlikeUserIdIsNotNullOrderByIdDesc(
////            Pageable pageable, char c,  Long id);//차단하는 유저가 없는 경우
//
    Page<StudyFeedList> findAllByUserIdAndDeleteyn(Pageable pageable, long user_id, char c);
//
//    List <StudyFeedList> findAllByuserIdAndUpdatedAtBetween(long userId, LocalDateTime startdate, LocalDateTime enddate);
//
//    Optional <StudyFeedList> findTop1ByuserIdAndUpdatedAtBetweenOrderByUpdatedAtDesc( long userId, LocalDateTime startdate, LocalDateTime enddate);
//
// //   long countByUser(User user);
//
////    List<StudyFeedList> findByUser(User user);
}
