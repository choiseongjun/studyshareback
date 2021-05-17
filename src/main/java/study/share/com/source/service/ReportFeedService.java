package study.share.com.source.service;

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

    public void reportFeedSave(FeedList reportfeed,String content) {

        Optional <User> feedOnwer = userRepository.findById(reportfeed.getUser().getId());//사용자 찾기
        feedOnwer.get().setReportedCnt(feedOnwer.get().getReportedCnt()+1);//신고수 1 증가
        ReportFeed reportFeed = new ReportFeed();
        reportFeed.setUser(feedOnwer.get());
        reportFeed.setFeedlist(reportfeed);
        reportFeed.setContent(content);
        reportFeedRepository.save(reportFeed);
    }

//    public void reportFeedDelete(FeedList reportfeed) {
//
//        Optional <User> feedOnwer = userRepository.findById(reportfeed.getUser().getId());//사용자 찾기
//        feedOnwer.get().setReportedCnt(feedOnwer.get().getReportedCnt()-1);//신고수 1 감소
//        reportFeedRepository.deleteById(reportfeed.getId());//피드신고 삭제
//    }


    public Page<ReportFeed> reportfeedlist(Pageable pageable) {
        return reportFeedRepository.findAll(pageable);
    }

}
