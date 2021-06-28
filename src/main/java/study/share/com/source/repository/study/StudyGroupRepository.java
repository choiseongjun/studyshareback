package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.study.StudyGroup;


public interface StudyGroupRepository extends JpaRepository<StudyGroup,Long> {
}
