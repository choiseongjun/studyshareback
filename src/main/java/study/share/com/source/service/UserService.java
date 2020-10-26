package study.share.com.source.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.Follow;
import study.share.com.source.model.UploadFile;
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
	
	public Optional<User> findUserNickname(String name) {
		Optional<User> user = userRepository.findByNickname(name);
		return user;
		
	}

	public void following(long id, Optional<User> user) {
		
		Optional<User> touser = userRepository.findById(id);
		
		Follow follow=new Follow();
		follow.setToUser(touser.get());
		follow.setFromUser(user.get());
		
		followRepository.save(follow);
		
	}

	public void canclefollowing(long id, Optional<User> user) {
		
		Optional<User> touser = userRepository.findById(id);
		
		followRepository.deleteByFromUserIdAndToUserId(user.get().getId(),touser.get().getId());
	}

	public List<Follow> followlist(Optional<User> user) {
		List<Follow> followlist=followRepository.findAllByToUserId(user.get().getId());
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

	public void findUserProfile(Long id) {
		// TODO Auto-generated method stub
		
	}

	



}
