package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import study.share.com.source.model.studygroup.StudyGroup;
import study.share.com.source.model.studygroup.StudyGroupMember;
import study.share.com.source.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyGroupMember,Long> {

    List<StudyGroupMember> findAllByStudyGroup(StudyGroup studyGroup);
    Optional<StudyGroupMember> findByStudyGroupAndMember(StudyGroup studyGroup, User user);
    void deleteByStudyGroupAndMember(StudyGroup studyGroup, User user);
}

