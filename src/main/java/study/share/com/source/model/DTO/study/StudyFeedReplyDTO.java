package study.share.com.source.model.DTO.study;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.DTO.feed.FeedReplyLikeDTO;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.study.StudyFeedReply;
import study.share.com.source.model.study.StudyFeedReplyLike;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
public class StudyFeedReplyDTO {

    private long id;

    private String content;

    private long feedId;

    private String nickname;

    private long userId;

    private long originNo;

    private long groupOrd;

    private long likeCnt;

    private long replyCnt;

    private List<StudyFeedReply> feedreReply;

    private LocalDateTime createdAt;

    private String userProfileImage;

    private List<StudyFeedReplyLikeDTO> feedReplyLikeList = new ArrayList<StudyFeedReplyLikeDTO>();

    private Stream<StudyFeedReplyLike> myFeedReplyLike;

    private long feedReplyLikeSize=0;

    public void addreplylike(StudyFeedReplyLike feedReplyLike){
        feedReplyLikeList.add(new StudyFeedReplyLikeDTO(feedReplyLike));
    }

    public StudyFeedReplyDTO (StudyFeedReply feedReply)
    {
        this.id=feedReply.getId();
        this.content=feedReply.getContent();
        this.feedId=feedReply.getStudyFeedList().getId();
        this.userId=feedReply.getUser().getId();
        this.originNo=feedReply.getOriginNo();
        this.groupOrd=feedReply.getGroupOrd();
        this.nickname=feedReply.getUser().getNickname();
        this.likeCnt = feedReply.getStudyfeedReplylike().size();
        this.createdAt = feedReply.getCreatedAt();

        List<StudyFeedReplyLike> feedReplyLikeList = feedReply.getStudyfeedReplylike();
        for(StudyFeedReplyLike feedReplyLike: feedReplyLikeList)
            this.addreplylike(feedReplyLike);
        this.feedReplyLikeSize = feedReplyLikeList.size();
        if(feedReply.getUser().getUserProfileImage()!=null)//유저 프로필 이미지가 있을때만
            this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();
        else
            this.userProfileImage =null;
    }

    public StudyFeedReplyDTO(StudyFeedReply feedReply, User user) {
        this.id=feedReply.getId();
        this.content=feedReply.getContent();
        this.feedId=feedReply.getStudyFeedList().getId();
        this.userId=feedReply.getUser().getId();
        this.originNo=feedReply.getOriginNo();
        this.groupOrd=feedReply.getGroupOrd();
        this.nickname=feedReply.getUser().getNickname();
        this.likeCnt = feedReply.getStudyfeedReplylike().size();
        this.createdAt = feedReply.getCreatedAt();
        this.feedReplyLikeSize = feedReply.getStudyfeedReplylike().size();
        this.myFeedReplyLike = feedReply.getStudyfeedReplylike().stream().filter(t->t.getUser().getId()==user.getId());
        if(user.getUserProfileImage()!=null)
            this.userProfileImage = user.getUserProfileImage().getSrc();
        else
            this.userProfileImage= null;
    }
}
