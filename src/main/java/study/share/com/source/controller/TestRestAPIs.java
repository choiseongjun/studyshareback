package study.share.com.source.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.OptionalLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.model.User;
import study.share.com.source.model.UserProfileImage;
import study.share.com.source.repository.UserProfileImageRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.service.UserService;

@RestController
public class TestRestAPIs {
	
	@Autowired
	UserProfileImageRepository userProfileImageRepository;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/api/test/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		return ">>> User Contents!";
	}

	@GetMapping("/api/test/pm")
	@PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
	public String projectManagementAccess() {
		return ">>> Board Management Project";
	}
	
	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}
	@PostMapping("/testprofile")
	public ResponseEntity<?> test(Principal principal){ 
		Optional<User> user = userService.findUserNickname(principal.getName());
		boolean userProfile = userProfileImageRepository.existsByUser(user.get());
		System.out.println("truesdfdsf");
		System.out.println(userProfile);
		long profileid = userProfileImageRepository.selectProfileId(user.get());
		System.out.println(profileid);
		//long userProfile = userRepository.findById(user.get().getId()).get().getUserProfileImage().getId();
		//OptionalLong.of(userProfile).orElseThrow(null);
		
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}
}