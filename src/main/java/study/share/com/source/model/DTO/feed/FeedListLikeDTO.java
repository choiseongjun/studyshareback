package study.share.com.source.model.DTO.feed;

import java.util.List;
import java.util.stream.Stream;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.feed.FeedLike;
import study.share.com.source.model.feed.FeedList;
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
	
	private Stream<FeedLike> myFeedlike;
	
	
	public FeedListLikeDTO(FeedList feedlist,User user) {
		this.setId(feedlist.getId());
		this.setContent(feedlist.getContent());
		this.setTotallike(feedlist.getFeedlike().size());
		this.setMyFeedlike(feedlist.getFeedlike().stream().filter(t->t.getUser().getId()==user.getId()));
//		this.setUser(feedlist.getUser());
	}
	
}
