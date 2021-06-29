package study.share.com.source.service.feed;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import study.share.com.source.model.*;
import study.share.com.source.model.DTO.feed.FeedReplyLikeDTO;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.study.StudyFeedReply;
import study.share.com.source.repository.feed.FeedListRepository;
import study.share.com.source.repository.feed.FeedReplyLikeRepository;
import study.share.com.source.repository.feed.FeedReplyRepository;

@Service
public class FeedReplyService {

	@Autowired
	FeedListRepository feedListRepository;
	@Autowired
	FeedReplyRepository feedReplyRepository;
	@Autowired
	FeedReplyLikeRepository feedReplyLikeRepository;

	
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

	public Page<FeedReply> getfeedreply(long id,Pageable pageable) {
		//List<FeedReply> feedreplylist=feedListRepository.findById(id).get().getFeedreply();
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다

		Page<FeedReply> feedreplylist = feedReplyRepository.findByFeedlist_idAndDeleteynAndGroupOrd(pageable,id,'N',0L);
		
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

	public FeedReplyLikeDTO likefeedreply(Optional<User> user, long id) {

		Optional<FeedReply> feedreply = feedReplyRepository.findById(id);
		feedreply.orElseThrow(()-> new NoSuchElementException("해당 댓글이 존재하지 않습니다"));

//		Optional<FeedReplyLike> userResult=feedReplyLikeRepository.findByuser_idAndId(user.get().getId(),id);
//		userResult.orElseThrow(()-> new NoSuchElementException("해당 유저가 좋아요를 나타낸 정보가 이미 존재합니다"));

		FeedReplyLike feedReplyLike = FeedReplyLike
				.builder()
				.user(user.get())
				.feedReply(feedreply.get())
				.build();
		feedReplyLikeRepository.save(feedReplyLike);
		FeedReplyLikeDTO feedReplyLikeDTO=new FeedReplyLikeDTO(feedReplyLike);
		return feedReplyLikeDTO;
	}

	public FeedReply addfeedcommentReply(long feedId, long id, Optional<User> user, String content) {

		FeedList feedList=new FeedList();
		feedList.setId(feedId);

		FeedReply feedReply=new FeedReply();
		feedReply.setUser(user.get());
		feedReply.setContent(content);
		feedReply.setFeedlist(feedList);
		feedReply.setDeleteyn('N');
		feedReply.setOriginNo(id);

		Optional<FeedReply> feedReplyonelist= feedReplyRepository.findTop1ByFeedlist_idAndOriginNoAndDeleteynOrderByGroupOrdDesc(feedId,id,'N');
		long groupOrd=1L;
		if(feedReplyonelist.isPresent())//이미 대댓글이 있는 경우
		{
			feedReply.setGroupOrd(feedReplyonelist.get().getGroupOrd()+1);
			groupOrd=feedReplyonelist.get().getGroupOrd()+1;
		}
		else//대댓글이 없는 경우
		{
			feedReply.setGroupOrd(1);
			Optional<FeedReply> feedReplyOnwer= feedReplyRepository.findById(id);
			feedReplyOnwer.get().setOriginNo(id);
		}
		feedReplyRepository.save(feedReply);

		FeedReply returnFeedReply =  feedReplyRepository.findByOriginNoAndGroupOrd(id,groupOrd);
		return returnFeedReply;
	}

	public void likeCanclefeedreply(Optional<User> user, long id) {

//		Optional<FeedReplyLike> userResult=feedReplyLikeRepository.findByIdAndUserId(id,user.get().getId());
//		userResult.orElseThrow(()-> new NoSuchElementException("해당 유저가 좋아요를 나타낸 정보가 존재하지 않습니다"));
		Optional<FeedReplyLike> userResult=feedReplyLikeRepository.findByFeedReplyIdAndUserId(id,user.get().getId());

		feedReplyLikeRepository.deleteById(userResult.get().getId());
	}

	public Page<FeedReply> getfeedrereply(Long id, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		pageable = PageRequest.of(0, 10);// 내림차순으로 정렬한다

		Page<FeedReply> feedreplylist = feedReplyRepository.findByOriginNoAndDeleteynOrderByGroupOrdDesc(pageable,id,'N');
		
		return feedreplylist;  
	}



	public List<FeedReply> feedReplyFeedLikeUserFind(Long id, User user) {
		List<FeedReply> feedreply = feedReplyRepository.findByIdAndFeedReplylikeUserId(id,user.getId());
		return feedreply;
	}

//	public Page<FeedReply> feedReplyFeedLikeUserFind(Long id, User user, Pageable pageable) {
//		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
//		pageable = PageRequest.of(0, 10);// 내림차순으로 정렬한다
//
//		Page<FeedReply> feedreplylist = feedReplyRepository.findByOriginNoAndDeleteynOrFeedReplylikeUserIdOrderByGroupOrdDesc(pageable,id,'N',user.getId());
//		
//		return feedreplylist; 
//	}

	public Page<FeedReply> feedReplyFeedLikeUserFind(Long id,User user, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
		Page<FeedReply> pageList = feedReplyRepository.findByFeedlist_idAndDeleteynOrFeedReplylikeUserIdAndFeedReplylikeUserIdIsNullAndFeedReplylikeUserIdIsNotNullOrderByGroupOrdDesc(pageable,id,'N',user.getId());
		return pageList; 
	}

}
