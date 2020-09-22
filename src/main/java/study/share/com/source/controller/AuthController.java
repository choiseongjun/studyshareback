package study.share.com.source.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
public class AuthController {

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
    public ResponseEntity<?>  authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

    	try {
    		Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserid(),
                            loginRequest.getPassword()
                    )
            );
           
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String ROLE=auth.getAuthorities().toString();
            String jwt = jwtProvider.generateJwtToken(authentication);
            Map<String, String> map =new HashMap<String, String>();
            map.put("ROLE", ROLE);
    		map.put("jwt", jwt);
            return ResponseEntity.ok(map);
    	}catch(Exception e) {
    		//e.printStackTrace();
    		return new ResponseEntity<>("아이디나 비밀번호를 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
    	System.out.println(signUpRequest.getNickname());
        if(userRepository.existsByNickname(signUpRequest.getNickname())) {
            return new ResponseEntity<String>("닉네임이 이미 존재합니다!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByUserid(signUpRequest.getUserid())) {
            return new ResponseEntity<String>("아이디가 이미 존재합니다!",
                    HttpStatus.BAD_REQUEST);
        }

        try {
        	// Creating user's account
            User user = new User(signUpRequest.getUserid(), signUpRequest.getNickname(),signUpRequest.getEmail(),
            		signUpRequest.getSex(), encoder.encode(signUpRequest.getPassword()));

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

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
            return new ResponseEntity<>("성공적으로 가입되었습니다.", HttpStatus.OK);
        }catch(Exception e) {
        	e.printStackTrace();
			return new ResponseEntity<>("서버 오류..다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        
    }
}