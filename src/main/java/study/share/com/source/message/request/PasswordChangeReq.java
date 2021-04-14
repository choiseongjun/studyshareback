package study.share.com.source.message.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeReq {

    private String email;
    private String userId;
    private String password;
}
