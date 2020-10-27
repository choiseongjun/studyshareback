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
	
	private long age;
	
	private List<FeedLike> feedlike;
	
	private long followerlistsize;
	
	private long followlistsize; 
	
	public UserResponse(User user, long followerlistsize, long followlistsize) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setSex(user.getSex());
		this.setIntroduce(user.getIntroduce());
		this.setAge(user.getAge());
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
	}

}