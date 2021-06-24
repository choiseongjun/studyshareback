package study.share.com.source.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.share.com.source.model.studygroup.StudyGroupMember;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudyGroupMemberDTO {

    private long id;
    private long userId;
    private long studyGroupId;
    private char ownerCheck;
    private boolean joinCondition;

    public StudyGroupMemberDTO(StudyGroupMember studyGroupMember)
    {
        this.id=studyGroupMember.getId();
        this.userId=studyGroupMember.getMember().getId();
        this.studyGroupId=studyGroupMember.getStudyGroup().getId();
        this.ownerCheck=studyGroupMember.getOwnerCheck();
        this.joinCondition=studyGroupMember.isJoinCondition();
    }


}
