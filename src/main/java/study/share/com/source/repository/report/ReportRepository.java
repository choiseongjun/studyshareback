package study.share.com.source.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.share.com.source.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {


}
