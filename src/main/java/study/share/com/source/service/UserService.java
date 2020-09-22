package study.share.com.source.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.share.com.source.model.User;
import study.share.com.source.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public Optional<User> findUserNickname(String name) {
		Optional<User> user = userRepository.findByNickname(name);
		return user;
		
	}

}
