package study.share.com.source.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.message.request.PasswordChangeReq;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;
import study.share.com.source.model.UserProfileImage;
import study.share.com.source.repository.FollowRepository;
import study.share.com.source.repository.UserProfileImageRepository;
import study.share.com.source.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowRepository followRepository;
	@Autowired
	UserProfileImageRepository userProfileImageRepository;
	@Autowired
    PasswordEncoder encoder;
	
	User returnUser = null;
	
	public Optional<User> findUserNickname(String name) {
		Optional<User> user = userRepository.findByNickname(name);
		return user;
		
	}

	public Follow following(long id, User user) {
		
		Optional<User> touser = userRepository.findById(id);
		
		Follow follow=new Follow();

		follow.setToUser(touser.get());//팔로잉 당하는 사람
		follow.setFromUser(user);//팔로잉 하는 사람 ->팔로잉 거는사람

		followRepository.save(follow);
		return follow;
	}

	public User canclefollowing(long id, Optional<User> user) {
		
		Optional<User> touser = userRepository.findById(id);
		
		followRepository.deleteByFromUserIdAndToUserId(user.get().getId(),touser.get().getId());
		return touser.get();
	}

	//팔로우 기록이 있는지 조회
	public Optional<Follow> findfollowing(long id, User user) {

		Optional<User> touser = userRepository.findById(id);

		Optional <Follow> followresult= followRepository.findByFromUserIdAndToUserId(user.getId(),touser.get().getId());
		return followresult;
	}


	public List<Follow> followerlist(Optional<User> user) {
		List<Follow> followlist=followRepository.findAllByToUserId(user.get().getId());
		return followlist;
	}
	public List<Follow> followlist(Optional<User> user) {
		List<Follow> followlist=followRepository.findAllByFromUserId(user.get().getId());
		return followlist;
	}

	public String saveProfileimage(MultipartFile file, String imgPath, User user) {

		boolean userProfile = userProfileImageRepository.existsByUser(user);
		
		
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
		
		Date time = new Date();
				
		String filesavedtime = format1.format(time);
		
		UserProfileImage uploadFile = new UserProfileImage();
		UUID uid2 = UUID.randomUUID();//랜덤uid2
		String originalFileName2 = file.getOriginalFilename();
		String fileExtension2 = originalFileName2.substring(originalFileName2.lastIndexOf(".") + 1).toLowerCase();
		//String imageNAME2 = filelists.getName();
		String savedName2 = uid2.toString();// 랜덤아이디
		
		if(userProfile==true) {
			long profileid = userProfileImageRepository.selectProfileId(user);
			userProfileImageRepository.deleteById(profileid);
		}
		uploadFile.setUser(user);
		uploadFile.setImageExtension(fileExtension2);
		uploadFile.setFilename(savedName2+filesavedtime);
		uploadFile.setRealname(originalFileName2);
		uploadFile.setSrc(imgPath);
		uploadFile.setFilesize(file.getSize());
		
		userProfileImageRepository.save(uploadFile);
		return savedName2+filesavedtime;
	}

	public void updateProfileImage(String profileimagePaths, long id) {
		UserProfileImage userProfileImage=userProfileImageRepository.findByFilename(profileimagePaths);
		User user = new User();
		user.setId(id);
		userProfileImage.setUser(user);
		userProfileImageRepository.save(userProfileImage);
	}

	public List<Follow> followinglist(Optional<User> user) {
		List<Follow> followlist=followRepository.findAllByFromUserId(user.get().getId());
		return followlist;
	}

	public Optional<User> findUserId(long id) {
		return userRepository.findById(id);
	}

	public void updateUser(User user) {
		userRepository.save(user);
	}

	public List<User> searchUserNickname(String nickname) {
		return userRepository.findByNicknameLike("%"+nickname+"%");
	}
	
	public User updateUserInfo(Optional<User> user, User userInfo) {
		
		user.ifPresent(updateUser->{
//			updateUser.setAge(userInfo.getAge());
			updateUser.setNickname(userInfo.getNickname());
			updateUser.setIntroduce(userInfo.getIntroduce());
//			updateUser.setPassword(encoder.encode(userInfo.getPassword2()));
			returnUser = userRepository.save(updateUser);
		});
		return returnUser;
	}


	public Optional<User> findByEmail(String email) {

		Optional <User> result=userRepository.findByEmail(email);
		result.orElseThrow(()-> new NoSuchElementException("해당 이메일을 사용한 유저가 존재하지 않습니다"));
		return result;
	}

	public Optional <User> checkpassword(String nickname,PasswordChangeReq passwordChangeReq) {
		Optional <User> result=userRepository.findByNickname(nickname);
		if(encoder.matches(passwordChangeReq.getPassword(),result.get().getPassword()))//비밀번호가 일치하는 경우에만 변경
			result.get().setPassword(encoder.encode(passwordChangeReq.getNewPassword()));
		return result;
	}

	public Optional <User> checkpasswordSet(PasswordChangeReq passwordChangeReq) {
		Optional <User> result=userRepository.findByUserid(passwordChangeReq.getUserId());
		result.get().setPassword(encoder.encode(passwordChangeReq.getNewPassword()));
		return result;
	}

	public Optional<User> findUserLoginId(String userid) {
		return userRepository.findByUserid(userid);
	}

	//마지막 로그인 시간 저장
	public void saveAccessTime(User user) {
		LocalDateTime dateTime= LocalDateTime.now();
		user.setLastaccesstime(dateTime);
		userRepository.save(user);
	}

	//알람 수신 허용 여부 변경
	public void saveAlarmCheck(User user, boolean check) {
		user.setAlarmCheck(check);
		userRepository.save(user);
	}

	public Optional <User> findFcmToken(String fcmToken) {
		return userRepository.findByFcmToken(fcmToken);
	}

	public void deleteFcm(String fcmToken, Optional<User> user) {
		Optional <User> result = userRepository.findByFcmToken(fcmToken);	
		
		if(result.get().getId()!=user.get().getId()) {
			result.ifPresent((deleteToken)->{
				deleteToken.setFcmToken("");
				userRepository.save(deleteToken);
			});			
		}

	}

	public void deleteFcm(Optional<User> otherFcmToken, String fcmToken) {
		Optional <User> deleteOther = userRepository.findById(otherFcmToken.get().getId());
		deleteOther.ifPresent((deleteFcm)->{
			deleteFcm.setFcmToken("");
			userRepository.save(deleteFcm);
		});
	}

}
