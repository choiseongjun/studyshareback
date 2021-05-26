package study.share.com.source.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.share.com.source.model.BlockedUser;
import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeed;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser,Long> {

    Optional<BlockedUser> findByUserIdAndBlockedUserId (long userid, long blockedUser);

    void deleteByUserIdAndBlockedUserId(long userid, long blockedUser);

    Page<BlockedUser> findAllByUserId(Pageable pageable,long userid);

    List<BlockedUser> findAllByUserId(long userid);
}
