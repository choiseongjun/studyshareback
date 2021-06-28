package study.share.com.source.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeedReply;
import study.share.com.source.repository.report.ReportFeedReplyRepository;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.utils.ReportConstant;

import java.util.Optional;

@Service
@Transactional
public class ReportFeedReplyService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ReportFeedReplyRepository reportFeedReplyRepository;

    public int reportFeedReplySave(FeedReply reportfeedReply, long reportId, User user) {

        Optional<User> feedOnwer = userRepository.findById(reportfeedReply.getUser().getId());//사용자 찾기
        Optional <ReportFeedReply> findReportReply = reportFeedReplyRepository.findByFeedreply_idAndReporter(reportfeedReply.getId(),user);
        if(findReportReply.isPresent())//이미 신고를 했던 경우
            return -1;
        else
        {
            long value=1L;
            if(reportId==0)
                value= ReportConstant.INAPPROPRIATE.getId();
            else if(reportId==1)
                value= ReportConstant.ABOMINATION.getId();
            else if(reportId==2)
                value= ReportConstant.ADVERTISEMENT.getId();
            else if(reportId==3)
                value= ReportConstant.BAD_LANGUAGE.getId();
            else
                value= ReportConstant.ETC.getId();

            ReportFeedReply reportFeedReply = new ReportFeedReply();
            reportFeedReply.setUser(feedOnwer.get());
            reportFeedReply.setFeedreply(reportfeedReply);
            reportFeedReply.setContent(value);
            reportFeedReply.setReporter(user);
            if(feedOnwer.get().getReportedCnt()==4)//5회째 누적 시 계정정지
                feedOnwer.get().setAccountSuspend(feedOnwer.get().getAccountSuspend()+1);

            feedOnwer.get().setReportedCnt(feedOnwer.get().getReportedCnt()+1);//신고수 1 증가
            reportFeedReplyRepository.save(reportFeedReply);
            return 0;
        }
    }

    public void reportFeedReplyDelete(FeedReply reportfeedReply,User user) {

        Optional <User> feedOnwer = userRepository.findById(reportfeedReply.getUser().getId());//사용자 찾기
        feedOnwer.get().setReportedCnt(feedOnwer.get().getReportedCnt()-1);//신고수 1 감소
        System.out.println("현재 신고당한 횟수: "+feedOnwer.get().getReportedCnt());
        System.out.println("피드 번호: "+reportfeedReply.getId()+"신고자 아이디: "+user.getId());
        Optional <ReportFeedReply> findReport = reportFeedReplyRepository.findByFeedreply_idAndReporter(reportfeedReply.getId(),user);//해당 신고내역 찾기
        System.out.println("찾은 신고 내용: "+findReport.get().getContent());
        reportFeedReplyRepository.deleteByIdAndReporter(findReport.get().getId(),user);//피드신고 삭제
    }


    public Page<ReportFeedReply> reportfeedlist(Pageable pageable) {
        return reportFeedReplyRepository.findAll(pageable);
    }

}
