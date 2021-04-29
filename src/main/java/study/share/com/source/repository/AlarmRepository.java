package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.share.com.source.model.AlarmHistory;

@Repository
public interface AlarmRepository extends JpaRepository<AlarmHistory, Long> {
}
