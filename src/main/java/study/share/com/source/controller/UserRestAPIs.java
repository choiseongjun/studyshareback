package study.share.com.source.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.repository.UserRepository;

@RestController
public class UserRestAPIs {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/api/auth/userinfo")
	@PreAuthorize("hasRole('USER')")
	public String userAccess(Principal principal) {
		
		System.out.println(principal);
		
		return ">>> User Contents!";
	}
}
