package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.FeedList;

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
