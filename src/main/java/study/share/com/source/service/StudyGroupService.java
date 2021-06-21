package study.share.com.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import study.share.com.source.message.request.StudyGroupReq;
import study.share.com.source.model.StudyGroup;
import study.share.com.source.model.User;

import study.share.com.source.repository.StudyGroupRepository;
import study.share.com.source.repository.UserRepository;

import java.util.Optional;

@Service
public class StudyGroupService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    StudyGroupRepository studyGroupRepository;


    public StudyGroup savegroup(StudyGroupReq groupreq, User owner)
    {
        StudyGroup group = new StudyGroup();
        group.setIntroduce(groupreq.getIntroduce());
        group.setOwner(owner);
        group.setTitle(groupreq.getTitle());
        group.setMemberLimit(groupreq.getMemberLimit());
        studyGroupRepository.save(group);
        return group;
    }

    public Optional <StudyGroup> findgroup(long id)
    {
        return studyGroupRepository.findById(id);
    }

    public void deletegroup(long id)
    {
        studyGroupRepository.deleteById(id);
    }

    public StudyGroup modifygroup(long id, StudyGroupReq studyGroupReq)
    {
        Optional<StudyGroup> group = findgroup(id);
        group.get().setIntroduce(studyGroupReq.getIntroduce());
        group.get().setTitle(studyGroupReq.getTitle());
        group.get().setMemberLimit(studyGroupReq.getMemberLimit());
        group.get().setRecruitStatus(studyGroupReq.isRecruitStatus());
        group.get().setJoinVerified(studyGroupReq.isJoinVerified());
        studyGroupRepository.save(group.get());
        return group.get();
    }

}
