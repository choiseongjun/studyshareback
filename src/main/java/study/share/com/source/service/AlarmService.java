package study.share.com.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.share.com.source.model.AlarmHistory;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.repository.AlarmRepository;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {

    @Autowired
    AlarmRepository alarmRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FeedListRepository feedListRepository;

    public void alarmReply(FeedReply feedReply,long id)
    {
        Optional <FeedList> feedList = feedListRepository.findById(id);
        Optional <User> user = userRepository.findById(feedList.get().getUser().getId());
        AlarmHistory alarmHistory= new AlarmHistory();
        alarmHistory.setFunction(1);
        alarmHistory.setContent(feedReply.getContent());
        alarmHistory.setToUser(user.get());
        alarmHistory.setFromUser(feedReply.getUser());
        alarmRepository.save(alarmHistory);
    }

    public void alarmlike(FeedList feedList, User user)
    {
        AlarmHistory alarmHistory= new AlarmHistory();

        alarmHistory.setFunction(2);
        alarmHistory.setContent(user.getUserid()+"님이 내 게시물을 좋아합니다.");
        alarmHistory.setToUser(feedList.getUser());
        alarmHistory.setFromUser(user);
        alarmRepository.save(alarmHistory);
    }

    public List<AlarmHistory> allalarmView(User user)
    {
        List<AlarmHistory> alarmHistoryList= alarmRepository.findByToUserId(user.getId());
        return alarmHistoryList;
    }

    public long alarmCount(long id)
    {
        long result= alarmRepository.countByToUserId(id);
        return result;
    }

}
