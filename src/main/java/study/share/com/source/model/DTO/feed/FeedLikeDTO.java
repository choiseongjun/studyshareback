package study.share.com.source.model.DTO.feed;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.feed.FeedList;

@Data
@NoArgsConstructor
public class FeedLikeDTO {
	
	private long id;
	
	private long userkey;
	
	public FeedLikeDTO(FeedList feedlikelist) {
		this.setId(feedlikelist.getId());
		this.setUserkey(feedlikelist.getUser().getId());	
	}

}
