package study.share.com.source.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.FeedReplyRepository;

@Service
public class FeedReplyService {

	@Autowired
	FeedListRepository feedListRepository;
	@Autowired
	FeedReplyRepository feedReplyRepository;
	
	public FeedList addfeedcomment(long id, Optional<User> user, String content) {
		
		FeedList feedList=new FeedList();
		feedList.setId(id);
		
		FeedReply feedReply=new FeedReply();
		feedReply.setUser(user.get());
		feedReply.setContent(content);
		feedReply.setFeedlist(feedList);
		feedReply.setDeleteyn('N');
		
		feedReplyRepository.save(feedReply);
		
		FeedList feedreplylist=feedListRepository.findById(id).get();
		
		return feedreplylist;
	}

	public List<FeedReply> getfeedreply(long id) {
		List<FeedReply> feedreplylist=feedListRepository.findById(id).get().getFeedreply();
		return feedreplylist;
	}

	public void removefeedcomment(long id, Optional<User> user) {
		Optional<FeedReply> feedreply=feedReplyRepository.findById(id);
		feedreply.ifPresent(selectList -> {
			selectList.setDeleteyn('Y');
			feedReplyRepository.save(selectList);
		});
	}

	public User checkusercomment(long id) {
		User replyuser = feedReplyRepository.findById(id).get().getUser();
		return replyuser;
	}

}
