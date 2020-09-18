package study.share.com.source.message.request;

import java.util.Set;

import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

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
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    
}