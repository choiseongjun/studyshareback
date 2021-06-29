package study.share.com.source.service.study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import study.share.com.source.model.DTO.feed.FeedReplyLikeDTO;
import study.share.com.source.model.DTO.study.StudyFeedReplyLikeDTO;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedReply;
import study.share.com.source.model.feed.FeedReplyLike;
import study.share.com.source.model.study.StudyFeedList;
import study.share.com.source.model.study.StudyFeedReply;
import study.share.com.source.model.study.StudyFeedReplyLike;
import study.share.com.source.repository.feed.FeedListRepository;
import study.share.com.source.repository.feed.FeedReplyLikeRepository;
import study.share.com.source.repository.feed.FeedReplyRepository;
import study.share.com.source.repository.study.StudyFeedListRepository;
import study.share.com.source.repository.study.StudyFeedReplyLikeRepository;
import study.share.com.source.repository.study.StudyFeedReplyRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class StudyFeedReplyService {


    @Autowired
    StudyFeedListRepository studyFeedListRepository;
    @Autowired
    StudyFeedReplyRepository studyFeedReplyRepository;
    @Autowired
    StudyFeedReplyLikeRepository studyFeedReplyLikeRepository;

    public Optional<StudyFeedReply> addfeedcomment(long id, Optional<User> user, String content) {

        StudyFeedList feedList=new StudyFeedList();
        feedList.setId(id);
        StudyFeedReply feedReply=new StudyFeedReply();
        feedReply.setUser(user.get());
        feedReply.setContent(content);
        feedReply.setStudyFeedList(feedList);
        feedReply.setDeleteyn('N');

        long feedreplyId = studyFeedReplyRepository.save(feedReply).getId();
        StudyFeedList feedreplylist=studyFeedListRepository.findById(id).get();

        Optional<StudyFeedReply> feedReplyonelist=studyFeedReplyRepository.findById(feedreplyId);
        feedReplyonelist.get().setFeedlistkey(id);

        return feedReplyonelist;
    }

    public Page<StudyFeedReply> getfeedreply(long id, Pageable pageable) {
        //List<FeedReply> feedreplylist=feedListRepository.findById(id).get().getFeedreply();
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
        Page<StudyFeedReply> feedreplylist = studyFeedReplyRepository.findByStudyFeedListIdAndDeleteynAndGroupOrd(pageable,id,'N',0L);
        return feedreplylist;

    }

    public void removefeedcomment(long id, Optional<User> user) {
        Optional<StudyFeedReply> feedreply=studyFeedReplyRepository.findById(id);
        feedreply.ifPresent(selectList -> {
            selectList.setDeleteyn('Y');
            studyFeedReplyRepository.save(selectList);
        });
    }

    public Optional<StudyFeedReply>checkusercomment(long id) {
        Optional<StudyFeedReply> studyFeedReply = studyFeedReplyRepository.findById(id);
        return studyFeedReply;
    }

    public long findPostId(long id) {
        return studyFeedReplyRepository.findById(id).get().getStudyFeedList().getId();
    }

    public StudyFeedReply updatefeedcomment(long id, Optional<User> user, String content) {
        Optional<StudyFeedReply> feedreply=studyFeedReplyRepository.findById(id);
        feedreply.ifPresent(selectList -> {
            selectList.setContent(content);
            studyFeedReplyRepository.save(selectList);
        });
        long feedreplyId = feedreply.get().getStudyFeedList().getId();
        feedreply.get().setFeedlistkey(feedreplyId);
        return feedreply.get();
    }

    public StudyFeedReplyLikeDTO likefeedreply(Optional<User> user, long id) {

        Optional<StudyFeedReply> feedreply = studyFeedReplyRepository.findById(id);
        feedreply.orElseThrow(()-> new NoSuchElementException("해당 댓글이 존재하지 않습니다"));

//		Optional<FeedReplyLike> userResult=feedReplyLikeRepository.findByuser_idAndId(user.get().getId(),id);
//		userResult.orElseThrow(()-> new NoSuchElementException("해당 유저가 좋아요를 나타낸 정보가 이미 존재합니다"));

        StudyFeedReplyLike feedReplyLike = StudyFeedReplyLike
                .builder()
                .user(user.get())
                .studyfeedReply(feedreply.get())
                .build();
        studyFeedReplyLikeRepository.save(feedReplyLike);
        StudyFeedReplyLikeDTO feedReplyLikeDTO=new StudyFeedReplyLikeDTO(feedReplyLike);
        return feedReplyLikeDTO;
    }

    public  StudyFeedReply addfeedcommentReply(long feedId, long id, Optional<User> user, String content) {

        StudyFeedList feedList=new StudyFeedList();
        feedList.setId(feedId);

        Optional< StudyFeedReply> feedReplyonelist= studyFeedReplyRepository.findById(id);
        feedReplyonelist.orElseThrow(() -> new NoSuchElementException("해당하는 댓글이 존재하지 않습니다"));

        StudyFeedReply feedReply=new  StudyFeedReply();
        feedReply.setUser(user.get());
        feedReply.setContent(content);
        feedReply.setStudyFeedList(feedList);
        feedReply.setDeleteyn('N');
        feedReply.setOriginNo(id);
        feedReply.setGroupOrd(feedReplyonelist.get().getGroupOrd()+1);
        int replyid =  studyFeedReplyRepository.updateorder(id,feedReplyonelist.get().getGroupOrd());
        StudyFeedReply returnFeedReply =  studyFeedReplyRepository.findByOriginNoAndGroupOrd(id,2);
        return returnFeedReply;
    }

    public void likeCanclefeedreply(Optional<User> user, long id) {

        Optional<StudyFeedReplyLike> userResult = studyFeedReplyLikeRepository.findByStudyFeedReplyIdAndUserId(id,user.get().getId());
        studyFeedReplyLikeRepository.deleteById(userResult.get().getId());
    }

    public Page<StudyFeedReply> getfeedrereply(Long id, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable = PageRequest.of(0, 10);// 내림차순으로 정렬한다

        Page<StudyFeedReply> feedreplylist =  studyFeedReplyRepository.findByOriginNoAndDeleteynOrderByGroupOrdDesc(pageable,id,'N');

        return feedreplylist;
    }



//    public List<FeedReply> feedReplyFeedLikeUserFind(Long id, User user) {
//        List<FeedReply> feedreply = feedReplyRepository.findByIdAndFeedReplylikeUserId(id,user.getId());
//        return feedreply;
//    }
//
////	public Page<FeedReply> feedReplyFeedLikeUserFind(Long id, User user, Pageable pageable) {
////		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
////		pageable = PageRequest.of(0, 10);// 내림차순으로 정렬한다
////
////		Page<FeedReply> feedreplylist = feedReplyRepository.findByOriginNoAndDeleteynOrFeedReplylikeUserIdOrderByGroupOrdDesc(pageable,id,'N',user.getId());
////
////		return feedreplylist;
////	}
//
//    public Page<FeedReply> feedReplyFeedLikeUserFind(Long id,User user, Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
//        pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");// 내림차순으로 정렬한다
//        Page<FeedReply> pageList = feedReplyRepository.findByFeedlist_idAndDeleteynOrFeedReplylikeUserIdAndFeedReplylikeUserIdIsNullAndFeedReplylikeUserIdIsNotNullOrderByGroupOrdDesc(pageable,id,'N',user.getId());
//        return pageList;
//    }
}
