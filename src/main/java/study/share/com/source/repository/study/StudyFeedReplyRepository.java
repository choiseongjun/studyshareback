package study.share.com.source.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.studygroup.StudyFeedReply;

import java.util.List;
import java.util.Optional;

public interface StudyFeedReplyRepository extends JpaRepository<StudyFeedReply,Long> {
    List<StudyFeedReply> findByStudyFeedListId(long id, Pageable pageable);

    //Page<StudyFeedReply> findByFeedlist_idAndDeleteyn(long id, Pageable pageable, char c);

    Optional<StudyFeedReply> findById (long id);

    @Modifying
    @Transactional
    @Query("UPDATE StudyFeedReply f set f.groupOrd=f.groupOrd+1 WHERE f.originNo=:originNo AND f.groupOrd>:groupOrd")
    int updateorder(@Param(value = "originNo") long originNo, @Param(value = "groupOrd") long groupOrd);

    Page<StudyFeedReply> findByStudyFeedListIdAndDeleteyn(Pageable pageable, long id, char c);

    StudyFeedReply findByOriginNoAndGroupOrd(long id, long l);

    Page<StudyFeedReply> findByStudyFeedListIdAndDeleteynAndGroupOrd(Pageable pageable, long id, char c, long l);

    Page<StudyFeedReply> findByOriginNoAndDeleteyn(Pageable pageable, Long id, char c);

    Page<StudyFeedReply> findByOriginNoAndDeleteynOrderByGroupOrdDesc(Pageable pageable, Long id, char c);

    List<StudyFeedReply> findByIdAndStudyfeedReplylikeUserId(Long id, Long id2);

  //  Page<StudyFeedReply> findByDeleteynOrFeedReplylikeUserIdOrderByGroupOrdDesc(Pageable pageable, char c, Long id);

  //  Page<StudyFeedReply> findByDeleteynOrFeedReplylikeUserIdOrFeedReplylikeUserIdIsNullOrderByGroupOrdDesc(Pageable pageable,
  //                                                                                                    char c, Long id);

  //  Page<StudyFeedReply> findByDeleteynAndFeedReplylikeUserIdOrderByGroupOrdDesc(Pageable pageable, char c, Long id);

    Page<StudyFeedReply> findByStudyFeedListIdAndDeleteynOrStudyfeedReplylikeUserIdAndStudyfeedReplylikeUserIdIsNullAndStudyfeedReplylikeUserIdIsNotNullOrderByGroupOrdDesc(
            Pageable pageable, Long id, char c, Long id2);
}
