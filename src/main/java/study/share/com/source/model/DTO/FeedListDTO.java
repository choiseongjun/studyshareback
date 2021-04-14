package study.share.com.source.model.DTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.*;

@Getter
@Setter
@Data
@NoArgsConstructor
public class FeedListDTO {
	private long id;
	
	private String content;
	
	private long totallike;
	
	private String nickname;
	
	private UserProfileImage userProfileImage;
	
	private List<UploadFile> uploadfile;
	
	private List<FeedLike> feedlike;
	
	private List<FeedReply> feedreply=new ArrayList<FeedReply>();

	private List<FeedTag> feedTagList =new ArrayList<>();

	private List<Tag> tagList= new ArrayList<>();
	
	private long feedreplysize;
	private LocalDateTime createdAt;

	
	
	public FeedListDTO(FeedList feedlist) {
		this.setId(feedlist.getId());
		this.setContent(feedlist.getContent());
		this.setTotallike(feedlist.getFeedlike().size());
		this.setUploadfile(feedlist.getUploadfile());
		this.setNickname(feedlist.getUser().getNickname());
		this.setUserProfileImage(feedlist.getUser().getUserProfileImage());
		this.setFeedreplysize(feedlist.getFeedreply().stream().filter(t->t.getDeleteyn()=='N').count());
		this.setFeedlike(feedlist.getFeedlike());
		this.setFeedreply(feedreply);
		this.setCreatedAt(feedlist.getCreatedAt());

//		for(int i=0;i<feedlist.getFeedlike().size();i++) {
//			this.setFeedlikeuser(feedlist.getFeedlike().get(i).getUser());	
//		}
		
		//this.setFeedlike(feedlist.getFeedlike());	
//		if(feedlist.getFeedlike() != null) {
//			this.setFeedlike(new ArrayList<FeedLikeDTO>());	
//		}		
	}

	
}
