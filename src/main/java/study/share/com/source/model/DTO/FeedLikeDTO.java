package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;

@Data
@NoArgsConstructor
public class FeedLikeDTO {
	
	private long id;
	
	private long userkey;
	
	public FeedLikeDTO(FeedLike feedlike) {
		this.setId(feedlike.getId());
		this.setUserkey(feedlike.getUser().getId());	
	}
}
