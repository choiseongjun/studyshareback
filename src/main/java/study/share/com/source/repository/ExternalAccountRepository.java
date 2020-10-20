package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.share.com.source.model.*;

public interface ExternalAccountRepository extends JpaRepository<ExternalAccount, ExternalAccountPK> {

    @Query(value = "SELECT user_id FROM user_external_account WHERE account_type=(:accountType) AND account_id=(:accountId)", nativeQuery = true)
    String findUserIdByAccountTypeAndAccountId(@Param("accountType") String accountType, @Param("accountId") String accountId);

}
