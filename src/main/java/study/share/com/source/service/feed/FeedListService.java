package study.share.com.source.service.feed;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import study.share.com.source.model.Tag;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.User;
import study.share.com.source.model.feed.FeedLike;
import study.share.com.source.model.feed.FeedList;
import study.share.com.source.model.feed.FeedTag;
import study.share.com.source.repository.feed.FeedLikeRepository;
import study.share.com.source.repository.feed.FeedListRepository;
import study.share.com.source.repository.feed.FeedReplyRepository;
import study.share.com.source.repository.feed.FeedTagRepository;
import study.share.com.source.repository.feed.TagRepository;
import study.share.com.source.repository.feed.UploadFileRepository;

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
	@Autowired
	TagRepository tagRepository;
	@Autowired
	FeedTagRepository feedTagRepository;

	
	/*2020-10-20 리턴값으로 사진 리스트 받는거 해결
	 *  addAll을 하여 방금 넣은 이미지를 리스트형식으로 하여 담아준다.
	 *  그러면 리턴할떄 방금 넣은이미지가 그대로 뿌려진다.
	 * */
	public FeedList saveFeed(Optional<User> user, String content, String file) {
		HttpServletRequest request = // 5
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		long feedid = feedListRepository.selectmaxid();
		FeedList feedList = new FeedList();
		feedList.setId(feedid);
		feedList.setContent(content);
		feedList.setUser(user.get());
		feedList.setDeleteyn('N');
		feedList.setTotallike(0);
		feedList.setIpaddress(request.getRemoteAddr());

		FeedList feed = feedListRepository.save(feedList);


		List<UploadFile> filelist = new ArrayList<UploadFile>();
		//Optional<FeedList> feedlist = feedListRepository.findById(feedid);
		if (file != null) {//파일이 있는경우
			String[] f = file.split(",");
			for (int i = 0; i < f.length; i++) {
				UploadFile fileone = uploadFileRepository.findBySrc(f[i]);
				fileone.setFeedlist(feedList);
				uploadFileRepository.save(fileone);
				filelist.add(fileone);
			}

			feed.getUploadfile().addAll(filelist);
		}

		return feed;
	}

	public Page<FeedList> listfeed(Pageable pageable) {
		return feedListRepository.findAllByDeleteynOrderByIdDesc(pageable, 'N');
	}

	public List<UploadFile> listgallary() {
		return uploadFileRepository.findAll();
	}

	public void deletefeed(long id) {
		Optional<FeedList> feedlist = feedListRepository.findById(id);
		feedlist.ifPresent(selectList -> {
			selectList.setDeleteyn('Y');
			feedListRepository.save(selectList);
		});
	}

	public Optional<FeedList> updatefeed(long id, String content, String file) {
		System.out.println("service id===="+id);
		Optional<FeedList> feedlist = feedListRepository.findById(id);
		feedlist.ifPresent(selectList -> {
			selectList.setContent(content);
			System.out.println("id===="+id);
			feedListRepository.save(selectList);
		});
		List<UploadFile> fileone2 = uploadFileRepository.findByFeedlistId(id);
		if (file != null) {//파일이 있는경우
			String[] f = file.split(",");
			for (int i = 0; i < f.length; i++) {
				UploadFile fileone = uploadFileRepository.findBySrc(f[i]);

				fileone.setFeedlist(feedlist.get());
				String src =uploadFileRepository.save(fileone).getSrc();//파일리스트에 키 업데이트

//				for(int deleteIdx=0;deleteIdx<fileone2.size();deleteIdx++) {
//					System.out.println("check@#$@$"+f[i]+"=="+fileone2.get(deleteIdx).getSrc());
//					if(!(f[i].equals(fileone2.get(deleteIdx).getSrc()))) {
//						System.out.println("file@#$@$"+file);
//						fileone2.get(deleteIdx).setFeedlist(null);
//						uploadFileRepository.save(fileone2.get(deleteIdx));
//					}
//				}
			}

		}
		

		return feedListRepository.findById(id);
	}

	public Optional<FeedList> likefeed(Optional<User> user, long id) {

		
	
		
		
		FeedList feedList = new FeedList();
		feedList.setId(id);

		FeedLike feedLike = new FeedLike();
		feedLike.setUser(user.get());
		feedLike.setFeedlist(feedList);
		feedLike.setUserkey(user.get().getId());
		feedLikeRepository.save(feedLike);

		//Optional<FeedList> feed = feedListRepository.findById(id);
		Optional<FeedList> feed = feedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());

//		feed.ifPresent(selectList -> {
//			selectList.setTotallike(feed.get().getTotallike()+1);
//			feedListRepository.save(selectList);
//		});
		return feed;
	}

	public Optional<FeedList> dislikefeed(Optional<User> user, long id) {
		FeedList feedlist = new FeedList();
		feedlist.setId(id);

		long feedlikeno = feedLikeRepository.findlikeno(user.get(), feedlist);
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
		List<FeedLike> feedlike = feedLikeRepository.findByfeedlistId(id);
		return feedlike;
	}

	public FeedList listfeedDetail(long id) {
		return feedListRepository.findById(id).get();
	}

	public Page<FeedList> mylistfeed(Pageable pageable, long user_id) {
		return feedListRepository.findAllByuser_id(pageable, user_id);
	}

	public List<FeedList> mylistfeedBydate( long user_id, LocalDateTime startdate,LocalDateTime enddate) {
		return feedListRepository.findAllByuserIdAndUpdatedAtBetween(user_id,startdate,enddate);
	}

	public Optional<FeedList> mylistfeedBydateOne( long user_id, LocalDateTime startdate,LocalDateTime enddate) {
		return feedListRepository.findTop1ByuserIdAndUpdatedAtBetweenOrderByUpdatedAtDesc(user_id,startdate,enddate);
	}

	public Page<FeedList> otherlistfeed(Pageable pageable, long user_id) {
		return feedListRepository.findAllByuser_idAndDeleteyn(pageable, user_id,'N');
	}

	public void extractHashTag(String content,FeedList feedList) {//해시태그 검출을 위한 함수
		Pattern p = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
		Matcher m = p.matcher(content);
		String extractHashTag = null;
		while (m.find()) {
			extractHashTag = sepecialCharacter_replace(m.group());
			if (extractHashTag != null) //해시태그 저장
			{
				Tag tag = new Tag();
				Optional <Tag> tagResult = tagRepository.findByname(extractHashTag);//태그 존재 여부 확인
				if (!tagResult.isPresent())//현재 존재하지 않는 태그는 새로 저장
				{
					tag.setName(extractHashTag);
					tagRepository.save(tag);
				}
				else //현재 존재하는 태그인 경우-> FeedTag에만 저장
					tag=tagResult.get();
				//FeedTag에 저장
				FeedTag feedTag=new FeedTag();
				feedTag.setFeedlist(feedList);
				feedTag.setTag(tag);
				feedTagRepository.save(feedTag);
			}
		}

	}
	public String remakeTag(String content) {//해시태그 검출을 위한 함수
		if(content==null)//입력 내용이 없는 경우 검출 하지 않음
			return null;
		Pattern p = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
		Matcher m = p.matcher(content);
		List tagSave = new ArrayList();
		String extractHashTag = null;
		while (m.find()) {
			extractHashTag = sepecialCharacter_replace(m.group());
			String Tag = "#"+extractHashTag;
			content=content.replace(Tag,"");
			if (extractHashTag != null) //해시태그가 있는 경우
			{
				content.replace(extractHashTag, "");//검출한 태그 내용에서 지우기
				tagSave.add(extractHashTag);//태그 저장
			}
		}
		for (Object object: tagSave)
		{
			String tag= (String) object;
			content+="#";
			content+=tag;
		}
		return content;
	}
	public String sepecialCharacter_replace(String str) {
		str = StringUtils.replaceChars(str, "-_+=!@#$%^&*()[]{}|\\;:'\"<>,.?/~`） ","");
		if(str.length() < 1) {
			return null;
		}
		return str;
	}
 
	public Page<FeedList> feedMylike(Pageable pageable, Optional<User> user, List UserId) {
		return feedListRepository.findDistinctAllByUserIdNotInAndDeleteynOrFeedlikeUserIdAndFeedlikeUserIdIsNullAndFeedlikeUserIdIsNotNullOrderByIdDesc(pageable, UserId ,'N',user.get().getId());
	}
 
	public Optional<FeedList> listMyFeedLikeFeedDetail(long id, Optional<User> user) {
		Optional<FeedList> existsMyFeedlike= feedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());
		
		if(existsMyFeedlike.isPresent()) {
			return feedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());
		}else {
			return feedListRepository.findById(id);
		}
	}

	public Page<FeedList> feedMylikeNotBlock(Pageable pageable, Optional<User> user) {
		return feedListRepository.findDistinctAllByDeleteynOrFeedlikeUserIdAndFeedlikeUserIdIsNullAndFeedlikeUserIdIsNotNullOrderByIdDesc(pageable,'N',user.get().getId());
	}

	public List<FeedList> FindFeedUser(User user) {
		return feedListRepository.findByUser(user);
	}

	public String getenddate(String date)
	{

		int year= Integer.parseInt(date.substring(0,4));
		int month=0;
		if(date.charAt(4)=='0')//10보다 작은 경우
			month=date.charAt(5)-'0';
		else
			month=Integer.parseInt(date.substring(4,6));
		Calendar cal = Calendar.getInstance();
		cal.set(year,month-1,1);
		int endday=cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		LocalDateTime localDateTime= LocalDateTime.of(year,month,endday,23,59,59);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String reuslt= localDateTime.format(formatter);
		return reuslt;
	}


}
