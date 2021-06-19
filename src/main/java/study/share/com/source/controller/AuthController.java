package study.share.com.source.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import study.share.com.source.message.request.LoginForm;
import study.share.com.source.message.request.PasswordChangeReq;
import study.share.com.source.message.request.SignUpForm;
import study.share.com.source.model.AccountType;
import study.share.com.source.model.Role;
import study.share.com.source.model.RoleName;
import study.share.com.source.model.User;
import study.share.com.source.model.DTO.AuthTokenDTO;
import study.share.com.source.repository.RoleRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.security.jwt.JwtProvider;
import study.share.com.source.security.services.UserPrinciple;
import study.share.com.source.service.AuthTokenService;
import study.share.com.source.service.ExternalAccountService;
import study.share.com.source.service.S3Service;
import study.share.com.source.service.UserService;
import study.share.com.source.service.VerificationTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends HttpServlet {

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

    @Autowired
    VerificationTokenService verificationTokenService;

    public static String tokenStorage = "";

    
    @ApiOperation(value="fcm토큰값 담아주기",notes="fcm토큰값 담아주기")
    @GetMapping("/set/token")
    public String setFcmToken(@RequestParam String token,@RequestParam String userId) throws IOException {
    	
    	String fcmToken = token;
    	Optional<User> user = userService.findUserLoginId(userId);
        if(fcmToken!=null){
            Optional <User> otherFcmToken = userService.findFcmToken(fcmToken);

        	 if(otherFcmToken.isPresent() && otherFcmToken!=null) {
             	if(otherFcmToken.get().getId()!=user.get().getId()) {//fcm있는 아이디와 로그인한 사람이 같지 않으면 
                     	userService.deleteFcm(otherFcmToken,fcmToken); 
                     	user.ifPresent(loginUser -> {
                         	loginUser.setFcmToken(fcmToken);
                         	userRepository.save(loginUser);
                 		});
                     }	
             }else{
             	user.ifPresent(loginUser -> {
                 	loginUser.setFcmToken(fcmToken);
                 	userRepository.save(loginUser);
         		});
             }
        }
		return token;
    }
    @ApiOperation(value="로그인",notes="로그인")
    @PostMapping("/signin")
    public ResponseEntity<?>  authenticateUser(@RequestBody LoginForm loginRequest, HttpServletResponse response) {        
       
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
            
            Optional<User> user = userService.findUserLoginId(loginRequest.getUserid());           

    		AuthTokenDTO authTokenDTO = authTokenService.createAuthToken(userPrincipal.getUsername());
	        String jwt = jwtProvider.generateJwtToken(authentication);
	        Map<String, String> map =new HashMap<String, String>();
	        map.put("ROLE", ROLE);
	        map.put("accessToken", authTokenDTO.getAccessToken());
	        map.put("jwt", authTokenDTO.getAccessToken());
	        map.put("refreshToken", authTokenDTO.getRefreshToken());
            return ResponseEntity.ok(map);
    	}catch(Exception e) {
    		//e.printStackTrace();
    		return new ResponseEntity<>("아이디나 비밀번호를 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	 
    }

    @ApiOperation(value="외부로그인(소셜)",notes="외부로그인(소셜)")
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
    @ApiOperation(value="유저아이디중복체크",notes="유저아이디중복체크")
    @PostMapping("/check/userid")
    public ResponseEntity<?> checkUserId(@RequestBody SignUpForm signUpRequest) {
    	 
		 if(userRepository.existsByUserid(signUpRequest.getUserid())) {
	         return new ResponseEntity<String>("이미 존재하는 아이디입니다.",HttpStatus.BAD_REQUEST);
	     }
		 return new ResponseEntity<String>("사용 가능한 아이디입니다.", HttpStatus.OK);

    }
    @ApiOperation(value="유저닉네임중복체크",notes="유저닉네임중복체크")
    @PostMapping("/check/username")
    public ResponseEntity<?> checkUserName(@RequestBody SignUpForm signUpRequest) {
    	 
    
		 if(userRepository.existsByNickname(signUpRequest.getNickname())) {
			 return new ResponseEntity<String>("이미 존재하는 닉네임입니다.",
	                    HttpStatus.BAD_REQUEST);
	     }
		 return new ResponseEntity<String>("사용 가능한 닉네임입니다.", HttpStatus.OK);
    }
    @ApiOperation(value="유저이메일중복체크",notes="유저이메일중복체크")
    @PostMapping("/check/useremail")
    public ResponseEntity<?> checkUserEmail(@RequestBody SignUpForm signUpRequest) {
    	
		 if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			 return new ResponseEntity<String>("이미 존재하는 이메일입니다",
	                    HttpStatus.BAD_REQUEST);
	     }
		 return new ResponseEntity<String>("사용 가능한 이메일입니다.", HttpStatus.OK);
    }
    @ApiOperation(value="회원가입",notes="회원가입")
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpForm signUpRequest) {
    	
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
            		signUpRequest.getSex(), encoder.encode(signUpRequest.getPassword()),signUpRequest.getAge());

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            strRoles.forEach(role -> {
            	switch(role) {
    	    		case "admin":
    	    			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
    	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
    	    			roles.add(adminRole);
    	    			
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
//            if(!signUpRequest.getProfileimagePaths().equals("undefiend")) {
//            	userService.updateProfileImage(signUpRequest.getProfileimagePaths(),userone.getId());
//            }
            
//            if(!(signUpRequest.getGtoken().equals(""))) {
//            	externalAccountService.connect(userone.getUserid(), signUpRequest.getAccountType(), signUpRequest.getGtoken());
//            }


            if(!StringUtils.isEmpty(user.getEmail())) {
                verificationTokenService.sendAuthEmail(user.getId(), user.getEmail());
            }

            return new ResponseEntity<>("성공적으로 가입되었습니다.", HttpStatus.OK);
        }catch(Exception e) {
        	e.printStackTrace();
			return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        
    }
    @ApiOperation(value="회원이미지 첨부",notes="회원이미지 첨부")
    @PostMapping("/profileimage")
	public ResponseEntity<?> saveProfileimage(@RequestPart(name = "images", required = false) MultipartFile file,Principal principal) throws IOException {
    	try {
    		
    		Optional<User> user = userService.findUserNickname(principal.getName());
    		
    		String imgPath="";
        	imgPath = s3Service.upload(file);
        	userService.saveProfileimage(file,imgPath,user.get());
        	
        	return new ResponseEntity<>(imgPath, HttpStatus.OK);
    	}catch(Exception e) { 
    		e.printStackTrace();
    		return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}
 
    @ApiOperation(value="토큰재발행",notes="토큰재발행")
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
    
    @ApiOperation(value="로그아웃",notes="로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() throws IOException {
//    	String tokenValue = StringUtils.split(token, " ")[1];
//        authTokenService.logout(tokenValue);
        try {
            
            return new ResponseEntity<>("success", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

    @ApiOperation(value="비밀번호 변경",notes="비밀번호 변경")
    @PostMapping("/change/password")
    public ResponseEntity<?> changepassword(@RequestBody PasswordChangeReq passwordChangeReq,Principal principal) throws IOException {
        try {
            Optional<User> result=userService.checkpassword(principal.getName(),passwordChangeReq);
            //result.orElseThrow(()->new NoSuchElementException("해당 유저 정보가 존재하지 않습니다"));

            return new ResponseEntity<>("비밀번호 변경 성공", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>("서버 오류..새로고침 후 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @ApiOperation(value="fcm토큰값 담아주기",notes="fcm토큰값 담아주기")
//    @GetMapping("/device/token")
//    public String getDeviceToken(@RequestParam String token) throws IOException {
//    	tokenStorage = token;
//		return token;
//    }
//    @ApiOperation(value="fcm토큰값 내려주기",notes="fcm토큰값 내려주기")
//    @GetMapping("/device/set/token")
//    public String setDeviceToken() {
//		return tokenStorage;
//    }
  
}