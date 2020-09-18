package study.share.com.source.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import study.share.com.source.message.request.LoginForm;
import study.share.com.source.message.request.SignUpForm;
import study.share.com.source.message.response.JwtResponse;
import study.share.com.source.model.Role;
import study.share.com.source.model.RoleName;
import study.share.com.source.model.User;
import study.share.com.source.repository.RoleRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.security.jwt.JwtProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

    	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserid(),
                        loginRequest.getPassword()
                )
        );
       
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if(userRepository.existsByNickname(signUpRequest.getNickname())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByUserid(signUpRequest.getUserid())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        System.out.println(signUpRequest.getUserid());
        System.out.println(signUpRequest.getNickname());
        System.out.println(signUpRequest.getPassword());
        
        // Creating user's account
        User user = new User(signUpRequest.getUserid(), signUpRequest.getNickname(),signUpRequest.getEmail(),
        		signUpRequest.getSex(), encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        System.out.println(strRoles);
      
        
        strRoles.forEach(role -> {
        	switch(role) {
	    		case "admin":
	    			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	    			roles.add(adminRole);
	    			
	    			break;
	    		case "manager":
	            	Role managerRole = roleRepository.findByName(RoleName.ROLE_MANAGER)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	            	roles.add(managerRole);
	            	
	    			break;
	    		default:
	        		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	        		roles.add(userRole);        			
        	}
        });
        
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");
    }
}