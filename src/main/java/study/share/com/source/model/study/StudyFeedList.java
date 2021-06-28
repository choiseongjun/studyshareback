package study.share.com.source.model.study;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_feedlist")
@Getter
@Setter
public class StudyFeedList extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "content")
    private String content;
    @Column(name = "total_like")
    private long totallike;
    @Column(name = "delete_yn",columnDefinition = "char(1) default 'N'")
    private char deleteyn;
    @Column(name = "ip_address")
    private String ipaddress;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private StudyGroup studyGroup;
    @OneToMany(orphanRemoval=true,mappedBy = "studyFeedList")
    private List<StudyUploadFile> uploadfile=new ArrayList<StudyUploadFile>();
    @OneToMany(orphanRemoval=true,mappedBy = "studyFeedList")
    private List<StudyFeedLike> studyfeedlike=new ArrayList<StudyFeedLike>();
    @OneToMany(orphanRemoval=true,mappedBy = "studyFeedList")
    private List<StudyFeedReply> studyfeedreply=new ArrayList<StudyFeedReply>();
    @OneToMany(orphanRemoval=true,mappedBy = "studyFeedList")
    private List<StudyFeedTag> studyfeedtag;
}
