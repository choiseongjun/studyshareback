package study.share.com.source.model.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.User;
import study.share.com.source.model.UserProfileImage;


@Data
@NoArgsConstructor
public class FeedListDTO {
	private long id;
	
	private String content;
	
	private long totallike;
	
	private long userKey;
	
	private User user;
	
	private UserProfileImage userProfileImage;
	
	private List<UploadFile> uploadfile;
	
	private List<FeedLike> feedlike;
	
	private List<FeedReply> feedreply;
	
	
	public FeedListDTO(FeedList feedlist) {
		this.setId(feedlist.getId());
		this.setContent(feedlist.getContent());
		this.setTotallike(feedlist.getFeedlike().size());
		this.setUploadfile(feedlist.getUploadfile());
		this.setUser(feedlist.getUser());
		this.setUserProfileImage(feedlist.getUser().getUserProfileImage());
		this.setFeedreply(feedlist.getFeedreply());
		this.setFeedlike(feedlist.getFeedlike());
//		for(int i=0;i<feedlist.getFeedlike().size();i++) {
//			this.setFeedlikeuser(feedlist.getFeedlike().get(i).getUser());	
//		}
		
		//this.setFeedlike(feedlist.getFeedlike());	
//		if(feedlist.getFeedlike() != null) {
//			this.setFeedlike(new ArrayList<FeedLikeDTO>());	
//		}		
	}

	
}
