package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedLike;

import study.share.com.source.model.studygroup.StudyFeedLike;
import study.share.com.source.model.studygroup.StudyFeedList;

import java.util.List;

public interface StudyFeedLikeRepository extends JpaRepository<StudyFeedLike,Long> {

    @Query("SELECT id FROM StudyFeedLike WHERE studyFeedList=(:studyfeedlist) AND user=(:user) ")
    long findlikeno(@Param("user") User user, @Param("studyfeedlist") StudyFeedList studyfeedlist);

    List<StudyFeedLike> findByStudyFeedListId(long id);
}