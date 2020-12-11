package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.AccountType;
import study.share.com.source.model.VerificationToken;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByAccountTypeAndToken(AccountType accountType, String token);

    VerificationToken findByUserNoAndAccountTypeAndAccountId(Long userNo, AccountType accountType, String accountId);

//    @Query("UPDATE VerificationToken SET expriesAt=(:expiresAt) WHERE id=(:id)")
//    void updateExpiresAt(Long id, LocalDateTime expiresAt);
}
