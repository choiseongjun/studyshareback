package study.share.com.source.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	public FeedReply addfeedcomment(long id, Optional<User> user, String content) {
		
		FeedList feedList=new FeedList();
		feedList.setId(id);
		
		FeedReply feedReply=new FeedReply();
		feedReply.setUser(user.get());
		feedReply.setContent(content);
		feedReply.setFeedlist(feedList);
		feedReply.setDeleteyn('N');
		
		long feedreplyId = feedReplyRepository.save(feedReply).getId();
		FeedList feedreplylist=feedListRepository.findById(id).get();
		
		Optional<FeedReply> feedReplyonelist=feedReplyRepository.findById(feedreplyId);
		feedReplyonelist.get().setFeedlistkey(id);
		
		return feedReplyonelist.get();
	}

	public List<FeedReply> getfeedreply(long id, Pageable pageable) {
		//List<FeedReply> feedreplylist=feedListRepository.findById(id).get().getFeedreply();
		List<FeedReply> feedreplylist = feedReplyRepository.findByFeedlist_idAndDeleteyn(id,pageable,'N');
		return feedreplylist;
//		return feedreplylist.stream().filter(t->t.getDeleteyn()=='N').sorted(new Comparator<FeedReply>() {
//			@Override
//			public int compare(FeedReply o1, FeedReply o2) {
//				long id1 = o1.getId();
//				long id2 = o2.getId();
//				if(id2<id1)
//					return 1;
//				else
//					return -1;
//			}
//		});

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

	public long findPostId(long id) {
		return feedReplyRepository.findById(id).get().getFeedlist().getId();
	}

	public FeedReply updatefeedcomment(long id, Optional<User> user, String content) {
		Optional<FeedReply> feedreply=feedReplyRepository.findById(id);
		feedreply.ifPresent(selectList -> {
			selectList.setContent(content);
			feedReplyRepository.save(selectList);
		});
		long feedreplyId = feedreply.get().getFeedlist().getId();
		feedreply.get().setFeedlistkey(feedreplyId);
		return feedreply.get();
	}

}
