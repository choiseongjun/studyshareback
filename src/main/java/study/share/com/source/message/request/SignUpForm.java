package study.share.com.source.message.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.AccountType;

@Getter
@Setter
public class SignUpForm {

    @NotBlank
    @Size(min = 3, max = 50)
    private String userid;
    @NotBlank
    @Size(min = 3, max = 50)
    private String nickname;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    
    private String sex;
    
    private String age;
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    private String profileimagePaths;
    
    private String gtoken;
    
    private AccountType accountType;

    
}