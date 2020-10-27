package study.share.com.source.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import study.share.com.source.model.FeedLike;
import study.share.com.source.model.FeedList;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.User;
import study.share.com.source.repository.FeedLikeRepository;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.FeedReplyRepository;
import study.share.com.source.repository.UploadFileRepository;

@Service
@Transactional
public class FeedListService {

	@Autowired
	FeedListRepository feedListRepository;
	@Autowired
	UploadFileRepository uploadFileRepository;
	@Autowired
	FeedLikeRepository feedLikeRepository;
	@Autowired
	FeedReplyRepository feedReplyRepository;
	
	/*2020-10-20 리턴값으로 사진 리스트 받는거 해결
	 *  addAll을 하여 방금 넣은 이미지를 리스트형식으로 하여 담아준다.
	 *  그러면 리턴할떄 방금 넣은이미지가 그대로 뿌려진다.
	 * */
	public FeedList saveFeed(Optional<User> user, String content, String file) {
		
		long feedid = feedListRepository.selectmaxid();
		
		FeedList feedList=new FeedList();
		feedList.setId(feedid);
		feedList.setContent(content);
		feedList.setUser(user.get());
		feedList.setDeleteyn('N');
		feedList.setTotallike(0);
		
		FeedList feed = feedListRepository.save(feedList);
		
		
		
		List<UploadFile> filelist = new ArrayList<UploadFile>();
		//Optional<FeedList> feedlist = feedListRepository.findById(feedid);
		if(file!=null) {//파일이 있는경우
			String[] f= file.split(",");
			for(int i=0;i<f.length;i++) {
				UploadFile fileone =uploadFileRepository.findBySrc(f[i]);
				fileone.setFeedlist(feedList);
				uploadFileRepository.save(fileone);
				filelist.add(fileone);				
			}
				
			feed.getUploadfile().addAll(filelist);	
		}

		return feed;
	}

	public List<FeedList> listfeed() {
		return feedListRepository.findAllByDeleteynOrderByIdDesc('N');
	}

	public List<UploadFile> listgallary() {
		return uploadFileRepository.findAll();
	}

	public void deletefeed(long id) {
		Optional<FeedList> feedlist=feedListRepository.findById(id);
		feedlist.ifPresent(selectList -> {
			selectList.setDeleteyn('Y');
			feedListRepository.save(selectList);
		});
	}

	public Optional<FeedList> updatefeed(long id, String content) {
		Optional<FeedList> feedlist=feedListRepository.findById(id);
		feedlist.ifPresent(selectList -> {
			selectList.setContent(content);
			feedListRepository.save(selectList);
		});
		
		return feedListRepository.findById(id);
	}

	public Optional<FeedList> likefeed(Optional<User> user, long id) {
		
		
		FeedList feedList=new FeedList();
		feedList.setId(id);
		
		FeedLike feedLike= new FeedLike();
		feedLike.setUser(user.get());
		feedLike.setFeedlist(feedList);
		feedLike.setUserkey(user.get().getId());
		feedLikeRepository.save(feedLike);
		 
		Optional<FeedList> feed = feedListRepository.findById(id);
//		feed.ifPresent(selectList -> {
//			selectList.setTotallike(feed.get().getTotallike()+1);
//			feedListRepository.save(selectList);
//		});
		return feed;
	}

	public Optional<FeedList> dislikefeed(Optional<User> user, long id) {
		FeedList feedlist=new FeedList();
		feedlist.setId(id);
		
		long feedlikeno = feedLikeRepository.findlikeno(user.get(),feedlist);
		feedLikeRepository.deleteById(feedlikeno);

		Optional<FeedList> feed = feedListRepository.findById(id);
//		feed.ifPresent(selectList -> {
//			selectList.setTotallike(feed.get().getTotallike()-1);
//			feedListRepository.save(selectList);
//		});
		return feed;
	}

	public Optional<FeedList> selectOne(long feedid) {
		return feedListRepository.findById(feedid);
	}

	public List<FeedLike> selectFeedlikelist(long id) {
		List<FeedLike> feedlike=feedLikeRepository.findByfeedlistId(id);
		return feedlike;
	}

	

}
