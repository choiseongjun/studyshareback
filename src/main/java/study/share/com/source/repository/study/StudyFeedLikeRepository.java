package study.share.com.source.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.share.com.source.model.User;

import study.share.com.source.model.study.StudyFeedLike;
import study.share.com.source.model.study.StudyFeedList;

import java.util.List;

public interface StudyFeedLikeRepository extends JpaRepository<StudyFeedLike,Long> {

    @Query("SELECT id FROM StudyFeedLike WHERE studyFeedList=(:studyfeedlist) AND user=(:user) ")
    long findlikeno(@Param("user") User user, @Param("studyfeedlist") StudyFeedList studyfeedlist);

    Page<StudyFeedLike> findByStudyFeedListId(long id, Pageable pageable);
}
