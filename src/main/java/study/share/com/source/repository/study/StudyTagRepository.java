package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.Tag;
import study.share.com.source.model.studygroup.StudyFeedTag;
import study.share.com.source.model.studygroup.StudyTag;

import java.util.Optional;

public interface StudyTagRepository extends JpaRepository<StudyTag,Long> {

    Optional<StudyTag> findByname(String name);
}
