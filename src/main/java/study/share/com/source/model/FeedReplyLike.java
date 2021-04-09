package study.share.com.source.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

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
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reply_id")
    private FeedReply feedReply;

    @Builder
    public FeedReplyLike(User user, FeedReply feedReply) {
        this.user=user;
        this.feedReply=feedReply;
    }
}
