package study.share.com.source.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginForm {
	
    @NotBlank
    @Size(min=3, max = 60)
    private String userid;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}