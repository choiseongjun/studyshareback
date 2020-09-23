package study.share.com.source.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import study.share.com.source.model.FeedList;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.User;
import study.share.com.source.repository.FeedListRepository;
import study.share.com.source.repository.UploadFileRepository;

@Service
@Transactional
public class FeedListService {

	@Autowired
	FeedListRepository feedListRepository;
	@Autowired
	UploadFileRepository uploadFileRepository;
	
	public Optional<FeedList> saveFeed(Optional<User> user, String content, String file) {
		
		long feedid = feedListRepository.selectmaxid();
		
		FeedList feedList=new FeedList();
		feedList.setId(feedid);
		feedList.setContent(content);
		feedList.setUser(user.get());
		
		feedListRepository.save(feedList);
		
		
		Optional<FeedList> feedlist = feedListRepository.findById(feedid);
		List<UploadFile> list=new ArrayList<UploadFile>();
		if(file!=null) {//파일이 있는경우
			String[] f= file.split(",");
		//	List<UploadFile> lists= new ArrayList<UploadFile>();	
			for(int i=0;i<f.length;i++) {
				UploadFile fileone =uploadFileRepository.findByFilepath(f[i]);
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
		return feedListRepository.findAll();
	}

}
