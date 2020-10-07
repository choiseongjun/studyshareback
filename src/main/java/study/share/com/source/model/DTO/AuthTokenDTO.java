package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthTokenDTO {
    String accessToken;
    String refreshToken;
}
