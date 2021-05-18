package study.share.com.source.service;

import com.google.firebase.database.core.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.share.com.source.model.FeedList;

import study.share.com.source.model.User;
import study.share.com.source.model.report.ReportFeed;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.ReportFeedRepository;
import study.share.com.source.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class ReportFeedService {

    @Autowired
    ReportFeedRepository reportFeedRepository;
    @Autowired
    FeedListRepository feedListRepository;
    @Autowired
    UserRepository userRepository;

    public int reportFeedSave(FeedList reportfeed,String content, User user) {

        Optional <User> feedOnwer = userRepository.findById(reportfeed.getUser().getId());//사용자 찾기
        Optional <ReportFeed> findReport = reportFeedRepository.findByFeedlistIdAndReporter(reportfeed.getId(),user);
        if(findReport.isPresent())//이미 신고를 했던 경우
            return -1;
        else
        {
            ReportFeed reportFeed = new ReportFeed();
            reportFeed.setUser(feedOnwer.get());
            reportFeed.setFeedlist(reportfeed);
            reportFeed.setContent(content);
            reportFeed.setReporter(user);
            if(feedOnwer.get().getReportedCnt()==4)//5회째 누적 시 계정정지
                feedOnwer.get().setAccountSuspend(feedOnwer.get().getAccountSuspend()+1);

            feedOnwer.get().setReportedCnt(feedOnwer.get().getReportedCnt()+1);//신고수 1 증가
            reportFeedRepository.save(reportFeed);
            return 0;
        }
    }

    public void reportFeedDelete(FeedList reportfeed,User user) {

        Optional <User> feedOnwer = userRepository.findById(reportfeed.getUser().getId());//사용자 찾기
        feedOnwer.get().setReportedCnt(feedOnwer.get().getReportedCnt()-1);//신고수 1 감소
        System.out.println("현재 신고당한 횟수: "+feedOnwer.get().getReportedCnt());
        System.out.println("피드 번호: "+reportfeed.getId()+"신고자 아이디: "+user.getId());
        Optional <ReportFeed> findReport = reportFeedRepository.findByFeedlistIdAndReporter(reportfeed.getId(),user);//해당 신고내역 찾기
        System.out.println("찾은 신고 내용: "+findReport.get().getContent());
        reportFeedRepository.deleteByIdAndReporter(findReport.get().getId(),user);//피드신고 삭제
    }


    public Page<ReportFeed> reportfeedlist(Pageable pageable) {
        return reportFeedRepository.findAll(pageable);
    }

}
