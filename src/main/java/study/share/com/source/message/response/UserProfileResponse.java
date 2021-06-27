package study.share.com.source.message.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.feed.FeedLike;
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
	
	private String age;
	
	private String email;
	
	private String accessToken;
	
	private String refreshToken;

	private List<FeedLike> feedlike;
	
	private long followerlistsize;
	
	private long followlistsize;
	
	private List<Follow> followlist;
	
	private List<BlockedUser> blockedUser;
	
	private long feedTotalCnt;

	private long followCheck;
	
	public UserProfileResponse(User user,String accessToken,String refreshToken) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setIntroduce(user.getIntroduce());
		this.setSex(user.getSex());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setProfileImage(user.getUserProfileImage().getSrc());		
		this.setAccessToken(accessToken);
		this.setRefreshToken(refreshToken);
	}
	public UserProfileResponse(User user,List<Follow> followlist, long followerlistsize, long followlistsize,List<BlockedUser> blockUserList,long feedTotalCnt) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setIntroduce(user.getIntroduce());
		this.setSex(user.getSex());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setProfileImage(user.getUserProfileImage().getSrc());		
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
		this.setBlockedUser(blockUserList);
		this.setFeedTotalCnt(feedTotalCnt);
		//this.setFollowlist(followlist);
	}
	public UserProfileResponse(User user, List<Follow> followlist, long followerlistsize, long followlistsize,long followCheck) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setIntroduce(user.getIntroduce());
		this.setSex(user.getSex());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setProfileImage(user.getUserProfileImage().getSrc());		
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
		this.setFollowCheck(followCheck);
	}
	
}
