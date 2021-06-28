package study.share.com.source.model.study;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "study_feedlike")
@Getter
@Setter
public class StudyFeedLike extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "study_feedlist_id")
    @JsonIgnore
    private StudyFeedList studyFeedList;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"study_feedlike","todolist","follow","roles"})
    private User user;

    private long userkey;
    @Transient
    private boolean tempFollow;
}
