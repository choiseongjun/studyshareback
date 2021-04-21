package study.share.com.source.message.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;

@Data
@NoArgsConstructor
public class UserResponse {
	
	private long id;
	
	private String nickname;
	
	private String sex;
	
	private String introduce;
	
	private String age;
	
	private String email;
	
	private List<FeedLike> feedlike;
	
	private long followerlistsize;
	
	private long followlistsize; 
	
	private List<Follow> followlist;
	
	public UserResponse(User user,List<Follow> followlist, long followerlistsize, long followlistsize) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setSex(user.getSex());
		this.setIntroduce(user.getIntroduce());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
		//this.setFollowlist(followlist);
	}

}