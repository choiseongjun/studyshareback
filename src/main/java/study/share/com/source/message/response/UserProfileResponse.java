package study.share.com.source.message.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;


/*유저 프로필이미지가 있는경우 */
@Data
@NoArgsConstructor
public class UserProfileResponse {


	private long id;
	
	private String nickname;
	
	private String sex;
	
	private String introduce;
	
	private String profileImage;
	
	private long age;

	private List<FeedLike> feedlike;
	
	private long followerlistsize;
	
	private long followlistsize;
	
	private List<Follow> followlist;
	
	public UserProfileResponse(User user,List<Follow> followlist, long followerlistsize, long followlistsize) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setIntroduce(user.getIntroduce());
		this.setSex(user.getSex());
		this.setAge(user.getAge());
		this.setProfileImage(user.getUserProfileImage().getSrc());		
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
		this.setFollowlist(followlist);
	}
	
}
