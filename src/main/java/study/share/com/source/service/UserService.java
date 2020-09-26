package study.share.com.source.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.Follow;
import study.share.com.source.model.User;
import study.share.com.source.repository.FollowRepository;
import study.share.com.source.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowRepository followRepository;
	
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



}
