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
	
	/*2020-09-24 리턴값으로 사진 리스트 받는거 해결안되고있음*/
	public Optional<FeedList> saveFeed(Optional<User> user, String content, String file) {
		
		long feedid = feedListRepository.selectmaxid();
		
		FeedList feedList=new FeedList();
		feedList.setId(feedid);
		feedList.setContent(content);
		feedList.setUser(user.get());
		feedList.setDeleteyn('N');
		feedList.setTotallike(0);
		
		feedListRepository.save(feedList);
		
		
		Optional<FeedList> feedlist = feedListRepository.findById(feedid);
		List<UploadFile> list=new ArrayList<UploadFile>();
		if(file!=null) {//파일이 있는경우
			String[] f= file.split(",");
		//	List<UploadFile> lists= new ArrayList<UploadFile>();	
			for(int i=0;i<f.length;i++) {
				UploadFile fileone =uploadFileRepository.findBySrc(f[i]);
				fileone.setFeedlist(feedList);
				uploadFileRepository.save(fileone);
//				UploadFile uploadfilelist=new UploadFile();
//				uploadfilelist.setFilepath(f[i]);
//				list.add(uploadfilelist);		
			}
			 
		}
		
		return feedlist;
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
			selectList.setContent(content);;
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
		feed.ifPresent(selectList -> {
			selectList.setTotallike(feed.get().getTotallike()-1);
			feedListRepository.save(selectList);
		});
		return feed;
	}

	

}
