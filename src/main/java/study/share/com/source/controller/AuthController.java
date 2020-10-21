package study.share.com.source.controller;

import java.io.IOException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.message.request.LoginForm;
import study.share.com.source.message.request.SignUpForm;
import study.share.com.source.model.AccountType;
import study.share.com.source.model.DTO.AuthTokenDTO;
import study.share.com.source.model.exception.GeneralErrorException;
import study.share.com.source.model.Role;
import study.share.com.source.model.RoleName;
import study.share.com.source.model.User;
import study.share.com.source.repository.RoleRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.security.jwt.JwtProvider;
import study.share.com.source.security.services.UserPrinciple;
import study.share.com.source.service.AuthTokenService;
import study.share.com.source.service.ExternalAccountService;
import study.share.com.source.service.S3Service;
import study.share.com.source.service.UserService;

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
    UserService userService;
    @Autowired
	S3Service s3Service;
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthTokenService authTokenService;

    @Autowired
    ExternalAccountService externalAccountService;

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

            UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
            AuthTokenDTO authTokenDTO = authTokenService.createAuthToken(userPrincipal.getUsername());
            String jwt = jwtProvider.generateJwtToken(authentication);
            Map<String, String> map =new HashMap<String, String>();
            map.put("ROLE", ROLE);
            map.put("accessToken", authTokenDTO.getAccessToken());
            map.put("jwt", authTokenDTO.getAccessToken());
            map.put("refreshToken", authTokenDTO.getAccessToken());
            return ResponseEntity.ok(map);
    	}catch(Exception e) {
    		//e.printStackTrace();
    		return new ResponseEntity<>("아이디나 비밀번호를 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    }


    @PostMapping("/signin_by_external")
    public ResponseEntity<?> signInByExternal(
            @RequestParam("account_type") AccountType accountType,
            @RequestParam("token") String token) {
        User user = externalAccountService.authentcateUser(accountType, token);
        AuthTokenDTO authTokenDTO = authTokenService.createAuthToken(user.getUserid());
        Map<String, String> map =new HashMap<String, String>();
        map.put("accessToken", authTokenDTO.getAccessToken());
        map.put("jwt", authTokenDTO.getAccessToken());
        map.put("refreshToken", authTokenDTO.getAccessToken());
        map.put("ROLE", "ROLE_USER");
        return ResponseEntity.ok(map);
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
    	
    	
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

            //long id = userRepository.selectusermaxid();
            user.setRoles(roles);
            User userone = userRepository.save(user);
            if(!signUpRequest.getProfileimagePaths().equals("undefiend")) {
            	userService.updateProfileImage(signUpRequest.getProfileimagePaths(),userone.getId());
            }
            if(signUpRequest.getGtoken()!=null) {
            	externalAccountService.connect(userone.getUserid(), signUpRequest.getAccountType(), signUpRequest.getGtoken());
            }
            return new ResponseEntity<>("성공적으로 가입되었습니다.", HttpStatus.OK);
        }catch(Exception e) {
        	e.printStackTrace();
			return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        
    }
    @PostMapping("/profileimage")
	public ResponseEntity<?> saveProfileimage(@RequestPart(name = "images", required = false) MultipartFile file) throws IOException {
    	try {
    		String imgPath="";
        	imgPath = s3Service.upload(file);
        	String ImgName = userService.saveProfileimage(file,imgPath);
        	System.out.println(ImgName);
        	return new ResponseEntity<>(ImgName, HttpStatus.OK);
    	}catch(Exception e) { 
    		e.printStackTrace();
    		return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam(name = "refresh_token") String refreshToken) throws IOException {
        try {
            AuthTokenDTO authTokenDTO = authTokenService.refresh(refreshToken);
            Map<String, String> map =new HashMap<String, String>();
            map.put("accessToken", authTokenDTO.getAccessToken());
            map.put("jwt", authTokenDTO.getAccessToken());
            map.put("refreshToken", authTokenDTO.getRefreshToken());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) throws IOException {

        try {
            String tokenValue = StringUtils.split(token, " ")[1];
            authTokenService.logout(tokenValue);
            return new ResponseEntity<>("success", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}