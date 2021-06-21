package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.StudyGroup;


public interface StudyGroupRepository extends JpaRepository<StudyGroup,Long> {
}
