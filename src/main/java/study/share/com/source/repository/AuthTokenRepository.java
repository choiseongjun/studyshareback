package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.share.com.source.model.AuthToken;
import study.share.com.source.model.FeedLike;

public interface AuthTokenRepository extends JpaRepository<AuthToken,Long> {
//    @Query("UPDATE auth_token SET accessToken = accessToken WHERE id=(:id)")
//    void updateAuthTokfnInfo(Long id, String accessToken, String refreshToken, int accessExpiresIn, int refreshExpiresIn);
}
