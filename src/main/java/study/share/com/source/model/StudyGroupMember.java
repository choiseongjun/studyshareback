package study.share.com.source.model;

import org.hibernate.annotations.ColumnDefault;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;

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
    @Column(name = "owner_check")
    @ColumnDefault("n")
    private char ownerCheck;
    @Column(name = "join_condition")
    @ColumnDefault("n")
    private char joinCondition;

}
