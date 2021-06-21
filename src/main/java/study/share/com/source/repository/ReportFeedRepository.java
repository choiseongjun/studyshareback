package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface ReportFeedRepository extends JpaRepository<ReportFeed,Long> {

    Page<ReportFeed> findAll(Pageable pageable);

    Optional<ReportFeed> findByFeedlistIdAndReporter(long feedlistId, User reporter);

    void deleteByIdAndReporter(long id, User reporter);

}
