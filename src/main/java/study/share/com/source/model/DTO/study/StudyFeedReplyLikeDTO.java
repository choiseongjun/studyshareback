package study.share.com.source.model.DTO.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.study.StudyFeedReplyLike;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudyFeedReplyLikeDTO {

    private long id;
    private long userId;
    private long replyId;

    public StudyFeedReplyLikeDTO (StudyFeedReplyLike feedReplyLike)
    {
        this.id= feedReplyLike.getId();
        this.userId=feedReplyLike.getUser().getId();
        this.replyId=feedReplyLike.getStudyFeedReply().getId();
    }
}
