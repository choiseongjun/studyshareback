package study.share.com.source.message.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.FeedLike;
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

	private List<FeedLike> feedlike;
	
	public UserProfileResponse(User user) {
		this.setId(user.getId());
		this.setIntroduce(user.getIntroduce());
		this.setSex(user.getSex());
		this.setProfileImage(user.getUserProfileImage().getSrc());		
		this.setFeedlike(user.getFeedlike());
	}
	
}
