package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken,Long> {
//    @Query("UPDATE auth_token SET accessToken = accessToken WHERE id=(:id)")
//    void updateAuthTokfnInfo(Long id, String accessToken, String refreshToken, int accessExpiresIn, int refreshExpiresIn);
}
