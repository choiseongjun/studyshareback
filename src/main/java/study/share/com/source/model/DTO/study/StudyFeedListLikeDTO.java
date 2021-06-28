package study.share.com.source.model.DTO.study;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.User;
import study.share.com.source.model.study.StudyFeedLike;
import study.share.com.source.model.study.StudyFeedList;

import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class StudyFeedListLikeDTO {
    private long id;

    private String content;

    private long totallike;

    private long userKey;

    private User user;

    private List<StudyFeedLike> feedlike;

    private Stream<StudyFeedLike> myFeedlike;


    public StudyFeedListLikeDTO(StudyFeedList feedlist,User user) {
        this.setId(feedlist.getId());
        this.setContent(feedlist.getContent());
        this.setTotallike(feedlist.getStudyfeedlike().size());
        this.setMyFeedlike(feedlist.getStudyfeedlike().stream().filter(t->t.getUser().getId()==user.getId()));
//		this.setUser(feedlist.getUser());
    }

}
