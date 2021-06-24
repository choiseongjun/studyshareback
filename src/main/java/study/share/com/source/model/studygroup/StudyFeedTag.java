package study.share.com.source.model.studygroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.Tag;
import study.share.com.source.model.feed.FeedList;

import javax.persistence.*;

@Entity
@Table(name = "study_feed_tag")
@Data
@Getter
@Setter
public class StudyFeedTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_feed_id")
    @JsonIgnore
    private StudyFeedList studyFeedList;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    @JsonIgnore
    private StudyTag studytag;
}
