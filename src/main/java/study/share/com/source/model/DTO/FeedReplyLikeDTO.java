package study.share.com.source.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.share.com.source.model.feed.FeedReplyLike;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FeedReplyLikeDTO {

    private long id;
    private long userId;
    private long replyId;

    public FeedReplyLikeDTO (FeedReplyLike feedReplyLike)
    {
        this.id= feedReplyLike.getId();
        this.userId=feedReplyLike.getUser().getId();
        this.replyId=feedReplyLike.getFeedReply().getId();
    }
}
