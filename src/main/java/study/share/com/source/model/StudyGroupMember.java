package study.share.com.source.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import study.share.com.source.model.common.DateAudit;

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
    @Column(name = "owner_check",columnDefinition = "char(1) default 'N'")
    private char ownerCheck;
    @Column(name = "join_condition",columnDefinition = "char(1) default 'N'")
    private char joinCondition;

}
