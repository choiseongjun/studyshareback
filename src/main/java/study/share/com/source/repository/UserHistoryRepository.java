package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.UserHistory;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {



}
