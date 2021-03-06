package study.share.com.source.model.DTO.feed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.User;

@Getter
@Setter
@NoArgsConstructor
public class FeedReplyDTO {

    private long id;

    private String content;

    private long feedId;

    private String nickname;

    private long userId;

    private long originNo;

    private long groupOrd;
    
    private long likeCnt;
    
    private long replyCnt;
    
    private List<FeedReplyDTO> feedreReply = new ArrayList<FeedReplyDTO>();
    
    private LocalDateTime createdAt;
    
    private String userProfileImage;
    
    private List<FeedReplyLikeDTO> feedReplyLikeList = new ArrayList<FeedReplyLikeDTO>();
    
    private Stream<FeedReplyLike> myFeedReplyLike;
    
    private long feedReplyLikeSize=0;

    public void addreplylike(FeedReplyLike feedReplyLike){
        feedReplyLikeList.add(new FeedReplyLikeDTO(feedReplyLike));
    }


    public FeedReplyDTO (FeedReply feedReply)
    {
        this.id=feedReply.getId();
        this.content=feedReply.getContent();
        this.feedId=feedReply.getFeedlist().getId();
        this.userId=feedReply.getUser().getId();
        this.originNo=feedReply.getOriginNo();
        this.groupOrd=feedReply.getGroupOrd();
        this.nickname=feedReply.getUser().getNickname();
        this.likeCnt = feedReply.getFeedReplylike().size();
		this.createdAt = feedReply.getCreatedAt();


        //피드 댓글 좋아요
        List<FeedReplyLike> feedReplyLikeList = feedReply.getFeedReplylike();
        for(FeedReplyLike feedReplyLike: feedReplyLikeList)
            this.addreplylike(feedReplyLike);
        this.feedReplyLikeSize = feedReplyLikeList.size();
        if(feedReply.getUser().getUserProfileImage()!=null)//유저 프로필 이미지가 있을때만
            this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();
        else
            this.userProfileImage =null;
    }

//    public FeedReplyDTO (FeedReply feedReply,List<FeedReply> feedReply2) {
//
//    	this.id=feedReply.getId();
//    	this.content=feedReply.getContent();
//        this.feedId=feedReply.getFeedlist().getId();
//        this.userId=feedReply.getUser().getId();
//        this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();
//        this.originNo=feedReply.getOriginNo();
//        this.groupOrd=feedReply.getGroupOrd();
//        this.nickname=feedReply.getUser().getNickname();
//        this.likeCnt = feedReply.getFeedReplylike().size();
//        	for(int i=0;i<feedReply2.size();i++) {
//        		if(feedReply.getId()==feedReply2.get(i).getOriginNo()) {
//        			this.feedreReply = feedReply2;
//        		}
//
//        	}
//
//    	this.createdAt = feedReply.getCreatedAt();
//		this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();
//    	this.feedReplyLikeSize = feedReply.getFeedReplylike().size();
//    }
//    public FeedReplyDTO (FeedReply feedReply,List<FeedReply> feedReply2,User user) {
//
//    	this.id=feedReply.getId();
//    	this.content=feedReply.getContent();
//        this.feedId=feedReply.getFeedlist().getId();
//        this.userId=feedReply.getUser().getId();
//        this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();
//        this.originNo=feedReply.getOriginNo();
//        this.groupOrd=feedReply.getGroupOrd();
//        this.nickname=feedReply.getUser().getNickname();
//        this.likeCnt = feedReply.getFeedReplylike().size();
//        if(feedReply2.size()>0) {
//        	for(int i=0;i<feedReply2.size();i++) {
//        		if(feedReply.getId()==feedReply2.get(i).getOriginNo()) {
//        			this.feedreReply = feedReply2;
//        		}
//
//        	}
//        }
//
//    	this.createdAt = feedReply.getCreatedAt();
//		this.myFeedReplyLike = feedReply.getFeedReplylike().stream().filter(t->t.getUser().getId()==user.getId());
//		this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();
//    	this.feedReplyLikeSize = feedReply.getFeedReplylike().size();
//    }
	public FeedReplyDTO(FeedReply feedReply, User user) {
		this.id=feedReply.getId();
        this.content=feedReply.getContent();
        this.feedId=feedReply.getFeedlist().getId();
        this.userId=feedReply.getUser().getId();
        this.originNo=feedReply.getOriginNo();
        this.groupOrd=feedReply.getGroupOrd();
        this.nickname=feedReply.getUser().getNickname();
        this.likeCnt = feedReply.getFeedReplylike().size();
		this.createdAt = feedReply.getCreatedAt();
		this.feedReplyLikeSize = feedReply.getFeedReplylike().size();
		this.myFeedReplyLike = feedReply.getFeedReplylike().stream().filter(t->t.getUser().getId()==user.getId());
		this.userProfileImage = feedReply.getUser().getUserProfileImage().getSrc();

		
	}

}
