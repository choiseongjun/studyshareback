package study.share.com.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.share.com.source.model.StudyGroup;
import study.share.com.source.model.StudyGroupMember;
import study.share.com.source.model.User;
import study.share.com.source.repository.StudyMemberRepository;
import study.share.com.source.repository.UserRepository;

@Service
public class StudyMemberService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StudyMemberRepository studyMemberRepository;


    public void savemember(User user, StudyGroup studyGroup, char owner)
    {
        StudyGroupMember studyMember = new StudyGroupMember();
        studyMember.setMember(user);
        studyMember.setStudyGroup(studyGroup);
        studyMember.setOwnerCheck(owner);
        studyMemberRepository.save(studyMember);
    }



}
