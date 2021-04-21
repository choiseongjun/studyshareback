package study.share.com.source.model.DTO;

import java.util.List;
import java.util.stream.Stream;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.User;

@Data
@NoArgsConstructor
public class FeedListLikeDTO {

	private long id;
	
	private String content;
	
	private long totallike;
	
	private long userKey;
	
	private User user;
	
	private List<FeedLike> feedlike;
	
	private List<FeedLike> myFeedlike;
	
	
	public FeedListLikeDTO(FeedList feedlist) {
		this.setId(feedlist.getId());
		this.setContent(feedlist.getContent());
		this.setTotallike(feedlist.getFeedlike().size());
		this.setMyFeedlike(feedlist.getFeedlike());
//		this.setUser(feedlist.getUser());
	}
	
}
