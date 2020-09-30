package study.share.com.source.message.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.User;

@Data
@NoArgsConstructor
public class UserResponse {
	
	private long id;
	
	private String nickname;
	
	private String sex;
	
	private String introduce;
	
	private List<FeedLike> feedlike;
	
	public UserResponse(User user) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setSex(user.getSex());
		this.setIntroduce(user.getIntroduce());
		this.setFeedlike(user.getFeedlike());
	}

}