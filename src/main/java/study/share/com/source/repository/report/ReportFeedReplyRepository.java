package study.share.com.source.repository.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeed;
import study.share.com.source.model.report.ReportFeedReply;

import java.util.Optional;

@Repository
public interface ReportFeedReplyRepository extends JpaRepository<ReportFeedReply,Long> {

    Optional<ReportFeedReply> findByFeedreply_idAndReporter(long feedreply_id, User reporter);
    void deleteByIdAndReporter(long id,User reporter);
    Page<ReportFeedReply> findAll(Pageable pageable);
}
