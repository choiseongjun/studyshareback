package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.FeedReply;

@Getter
@Setter
public class FeedReplyDTO {

    private long id;

    private String content;

    private long feedId;

    private String nickname;

    private long userId;

    private long origin_no;

    private long group_ord;

    public FeedReplyDTO (FeedReply feedReply)
    {
        this.id=feedReply.getId();
        this.content=feedReply.getContent();
        this.feedId=feedReply.getFeedlist().getId();
        this.userId=feedReply.getUser().getId();
        this.origin_no=feedReply.getOrigin_no();
        this.group_ord=feedReply.getGroup_ord();
        this.nickname=feedReply.getUser().getNickname();
    }

}
