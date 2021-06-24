package study.share.com.source.model.studygroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedReplyLike;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_feedreply")
@Getter
@Setter
public class StudyFeedReply extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "content")
    private String content;
    @Column(name = "delete_yn",columnDefinition = "char(1) default 'N'")
    private char deleteyn;
    @Column(name = "ip_address")
    private String ipaddress;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "study_feedlist_id")
    @JsonIgnore
    private StudyFeedList studyFeedList;
    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"study_feedlike","follow","todolist","userProfileImage"})
    @JoinColumn(name = "user_id")
    private User user;
    @Transient//댓글 추가할때 상태변화로 바로 넣을수있도록 임시조치
    private long feedlistkey;

    @Column(name = "origin_no")
    @ColumnDefault("0")
    private long originNo;

    @Column(name = "group_ord")
    @ColumnDefault("0")
    private long groupOrd;

    @OneToMany(orphanRemoval=true, mappedBy = "studyFeedReply")
    private List<StudyFeedReplyLike> studyfeedReplylike=new ArrayList<StudyFeedReplyLike>();
}
