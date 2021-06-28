package study.share.com.source.model.DTO.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.share.com.source.model.study.StudyGroup;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudyGroupDTO {

    private long id;
    private long memberLimit;
    private String title;
    private String introduce;
    private boolean joinVerified;
    private boolean recruitStatus;
    private long onwerId;

    public StudyGroupDTO (StudyGroup studyGroup)
    {
        this.id=studyGroup.getId();
        this.introduce=studyGroup.getIntroduce();
        this.memberLimit=studyGroup.getMemberLimit();
        this.title=studyGroup.getTitle();
        this.onwerId=studyGroup.getOwner().getId();
        this.joinVerified=studyGroup.isJoinVerified();
        this.recruitStatus=studyGroup.isRecruitStatus();
    }

}
