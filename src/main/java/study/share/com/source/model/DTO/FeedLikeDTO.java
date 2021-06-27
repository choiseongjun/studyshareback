package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.studygroup.StudyFeedList;

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
