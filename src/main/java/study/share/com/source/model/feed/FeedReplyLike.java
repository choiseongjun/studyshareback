package study.share.com.source.model.feed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;
import study.share.com.source.model.feed.FeedReply;

import javax.persistence.*;

@Entity
@Table(name = "feedreplylike")
@Getter
@NoArgsConstructor
public class FeedReplyLike extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"feedlike","todolist","follow","roles"})	
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reply_id")
	@JsonIgnore
    private FeedReply feedReply;

    @Builder
    public FeedReplyLike(User user, FeedReply feedReply) {
        this.user=user;
        this.feedReply=feedReply;
    }
}
