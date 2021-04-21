package study.share.com.source.model.DTO;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.FeedReply;

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
    
    private List<FeedReply> feedreReply;
    
    private LocalDateTime createdAt;
    

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

    }
    public FeedReplyDTO (FeedReply feedReply,List<FeedReply> feedReply2) {
//    	for(int i=0;i<feedReply.size();i++) {
//    		
//    		for(int j=0;j<feedReply2.size();j++) {
//    			if(feedReply.get(i).getId()==feedReply2.get(j).getOriginNo()) {
////        			feedReply.addAll(feedReply2);
//        			System.out.println("feedReply+="+feedReply);
//
//        			this.feedreply =feedReply2;
////        					 Stream.of(feedReply, feedReply2)
////	                            .flatMap(Collection::stream)
////	                            .collect(Collectors.toList());
//    			}
//    		}
//    	}
    	this.id=feedReply.getId();	
    	this.content=feedReply.getContent();
        this.feedId=feedReply.getFeedlist().getId();
        this.userId=feedReply.getUser().getId();
        this.originNo=feedReply.getOriginNo();
        this.groupOrd=feedReply.getGroupOrd();
        this.nickname=feedReply.getUser().getNickname();
        this.likeCnt = feedReply.getFeedReplylike().size();
    	for(int i=0;i<feedReply2.size();i++) {
    		if(feedReply.getId()==feedReply2.get(i).getOriginNo()) {
    			this.feedreReply = feedReply2;		
    		}
    	}
    	this.createdAt = feedReply.getCreatedAt();
    	
    }

}
