package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.studygroup.StudyFeedReplyLike;

import java.util.Optional;

public interface StudyFeedReplyLikeRepository extends JpaRepository<StudyFeedReplyLike, Long> {

    Optional<StudyFeedReplyLike> findByIdAndUserId(long id, Long id2);

    Optional<StudyFeedReplyLike> findByStudyFeedReplyIdAndUserId(long id, Long id2);

    void deleteByStudyFeedReplyId(long id);

    void deleteByStudyFeedReplyIdAndUserId(long id, Long id2);
}
