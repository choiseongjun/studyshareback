package study.share.com.source.model.studygroup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import study.share.com.source.model.common.DateAudit;
import study.share.com.source.model.feed.FeedList;

import javax.persistence.*;

@Entity
@Table(name = "study_uploadfile")
@Data
@EqualsAndHashCode(callSuper=false)
public class StudyUploadFile extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JsonIgnore
    private long userkey;
    @JsonIgnore
    private String filename;
    @JsonIgnore
    private String realname;
    @JsonIgnore
    private long filesize;

    private String src;
    @JsonIgnore
    private String ImageExtension;

    @ManyToOne(optional = true)
    @JoinColumn(name = "study_feedlist_id")
    @JsonIgnore
    private StudyFeedList studyFeedList;

    public StudyUploadFile() {
    }

    @Builder
    public StudyUploadFile(Long id, String filename, String src) {
        this.id = id;
        this.filename=filename;
        this.src = src;
    }


}
