package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.study.StudyTag;

import java.util.Optional;

public interface StudyTagRepository extends JpaRepository<StudyTag,Long> {

    Optional<StudyTag> findByname(String name);
}
