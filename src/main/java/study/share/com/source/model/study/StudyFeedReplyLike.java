package study.share.com.source.model.study;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;


import javax.persistence.*;

@Entity
@Table(name = "study_feedreplylike")
@Getter
@NoArgsConstructor
public class StudyFeedReplyLike extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"study_feedlike","todolist","follow","roles"})
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "study_reply_id")
    @JsonIgnore
    private StudyFeedReply studyFeedReply;

    @Builder
    public StudyFeedReplyLike(User user, StudyFeedReply studyfeedReply) {
        this.user=user;
        this.studyFeedReply=studyfeedReply;
    }
}
