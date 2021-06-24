package study.share.com.source.model.studygroup;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;
import study.share.com.source.model.studygroup.StudyGroup;

import javax.persistence.*;

@Entity
@Table(name = "study_group_member")
@Getter
@Setter
public class StudyGroupMember extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User member;
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private StudyGroup studyGroup;
    @Column(name = "owner_check",columnDefinition = "char default 'n'")
    private char ownerCheck;
    @Column(name = "join_condition")
    @ColumnDefault("1")
    private boolean joinCondition;

}
