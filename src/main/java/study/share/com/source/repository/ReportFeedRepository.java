package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.share.com.source.model.report.ReportFeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ReportFeedRepository extends JpaRepository<ReportFeed,Long> {

    Page<ReportFeed> findAll(Pageable pageable);

}
