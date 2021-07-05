package study.share.com.source.message.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.feed.FeedLike;
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
	
	private List<BlockedUser> blockedUser;
	
	private long feedTotalCnt;

	private long followerCheck;

	private long grade;

	private long point;
	
	public UserResponse(User user,List<Follow> followlist, long followerlistsize, long followlistsize,List<BlockedUser> blockUserList, long feedTotalCnt) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setSex(user.getSex());
		this.setIntroduce(user.getIntroduce());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
		this.setBlockedUser(blockUserList);
		this.setFeedTotalCnt(feedTotalCnt);
		//this.setFollowlist(followlist);
		this.setGrade(user.getGrade());
		this.setPoint(user.getPoint());
	}

	public UserResponse(User user, List<Follow> followlist, long followerlistsize, long followlistsize) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setSex(user.getSex());
		this.setIntroduce(user.getIntroduce());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);

	}

	public UserResponse(User user, List<Follow> followlist, long followerlistsize, long followlistsize,
			long feedTotalCnt,long followCheck ) {
		this.setId(user.getId());
		this.setNickname(user.getNickname());
		this.setSex(user.getSex());
		this.setIntroduce(user.getIntroduce());
		this.setAge(user.getAge());
		this.setEmail(user.getEmail());
		this.setFeedlike(user.getFeedlike());
		this.setFollowerlistsize(followerlistsize);
		this.setFollowlistsize(followlistsize);
		this.setFeedTotalCnt(feedTotalCnt);
		this.setFollowerCheck(followCheck);
		this.setGrade(user.getGrade());
		this.setPoint(user.getPoint());
	}

}