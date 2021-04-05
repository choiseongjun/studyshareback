package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import study.share.com.source.model.Report;
import study.share.com.source.model.TodoList;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {


}
