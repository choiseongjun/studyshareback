package study.share.com.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.share.com.source.model.AlarmHistory;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.repository.AlarmRepository;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.UserRepository;

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
        alarmHistory.setContent(feedList.getContent());
        alarmHistory.setToUser(feedList.getUser());
        alarmHistory.setFromUser(user);
        alarmRepository.save(alarmHistory);
    }


}
