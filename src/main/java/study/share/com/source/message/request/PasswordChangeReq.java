package study.share.com.source.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeReq {
    private String userId;
    private String password;
    private String newPassword;
}
