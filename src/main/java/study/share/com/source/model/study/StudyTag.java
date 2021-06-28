package study.share.com.source.model.study;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "study_tag")
@Data
public class StudyTag extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    //	    @ManyToMany(mappedBy = "tags")
//	    private Set<Moim> moims = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval=true,mappedBy = "studytag",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudyFeedTag> tag;

}
