package study.share.com.source.service;

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
import study.share.com.source.model.studygroup.*;
import study.share.com.source.repository.TagRepository;
import study.share.com.source.repository.UploadFileRepository;
import study.share.com.source.repository.feed.FeedLikeRepository;
import study.share.com.source.repository.feed.FeedListRepository;
import study.share.com.source.repository.feed.FeedReplyRepository;
import study.share.com.source.repository.feed.FeedTagRepository;
import study.share.com.source.repository.study.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class StudyFeedListService {

    @Autowired
    StudyFeedListRepository studyFeedListRepository;
    @Autowired
    StudyUploadFileRepository studyUploadFileRepository;
    @Autowired
    StudyFeedLikeRepository studyFeedLikeRepository;
    @Autowired
    StudyFeedReplyRepository studyFeedReplyRepository;
    @Autowired
    StudyTagRepository studyTagRepository;
    @Autowired
    StudyFeedTagRepository studyFeedTagRepository;


    /*2020-10-20 리턴값으로 사진 리스트 받는거 해결
     *  addAll을 하여 방금 넣은 이미지를 리스트형식으로 하여 담아준다.
     *  그러면 리턴할떄 방금 넣은이미지가 그대로 뿌려진다.
     * */
    public StudyFeedList saveFeed( String content, String file, StudyGroup studyGroup, StudyGroupMember studyGroupMember) {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        long feedid = studyFeedListRepository.selectmaxid();
        StudyFeedList feedList = new StudyFeedList();
        feedList.setId(feedid);
        feedList.setContent(content);
       // feedList.setStudyGroupMember(studyGroupMember);
        feedList.setDeleteyn('N');
        feedList.setTotallike(0);
        feedList.setIpaddress(request.getRemoteAddr());
        feedList.setStudyGroup(studyGroup);

        StudyFeedList feed = studyFeedListRepository.save(feedList);

        List<StudyUploadFile> filelist = new ArrayList<StudyUploadFile>();
        //Optional<FeedList> feedlist = studyFeedListRepository.findById(feedid);
        if (file != null) {//파일이 있는경우
            String[] f = file.split(",");
            for (int i = 0; i < f.length; i++) {
                StudyUploadFile fileone = studyUploadFileRepository.findBySrc(f[i]);
                fileone.setStudyFeedList(feedList);
                studyUploadFileRepository.save(fileone);
                filelist.add(fileone);
            }

            feed.getUploadfile().addAll(filelist);
        }

        return feed;
    }

    public Page<StudyFeedList> listfeed(Pageable pageable) {
        return studyFeedListRepository.findAllByDeleteynOrderByIdDesc(pageable, 'N');
    }

    public List<StudyUploadFile> listgallary() {
        return studyUploadFileRepository.findAll();
    }

    public void deletefeed(long id) {
        Optional<StudyFeedList> feedlist = studyFeedListRepository.findById(id);
        feedlist.ifPresent(selectList -> {
            selectList.setDeleteyn('Y');
            studyFeedListRepository.save(selectList);
        });
    }

//    public Optional<StudyFeedList> updatefeed(long id, String content, String file) {
//        System.out.println("service id===="+id);
//        Optional<StudyFeedList> feedlist = studyFeedListRepository.findById(id);
//        feedlist.ifPresent(selectList -> {
//            selectList.setContent(content);
//            System.out.println("id===="+id);
//            studyFeedListRepository.save(selectList);
//        });
//        List<StudyUploadFile> fileone2 = studyUploadFileRepository.findByFeedlistId(id);
//        if (file != null) {//파일이 있는경우
//            String[] f = file.split(",");
//            for (int i = 0; i < f.length; i++) {
//                UploadFile fileone = studyUploadFileRepository.findBySrc(f[i]);
//
//                fileone.setFeedlist(feedlist.get());
//                String src =studyUploadFileRepository.save(fileone).getSrc();//파일리스트에 키 업데이트
//
////				for(int deleteIdx=0;deleteIdx<fileone2.size();deleteIdx++) {
////					System.out.println("check@#$@$"+f[i]+"=="+fileone2.get(deleteIdx).getSrc());
////					if(!(f[i].equals(fileone2.get(deleteIdx).getSrc()))) {
////						System.out.println("file@#$@$"+file);
////						fileone2.get(deleteIdx).setFeedlist(null);
////						studyUploadFileRepository.save(fileone2.get(deleteIdx));
////					}
////				}
//            }
//
//        }
//
//
//        return studyFeedListRepository.findById(id);
//    }
//
//    public Optional<StudyFeedList> likefeed(Optional<User> user, long id) {
//
//
//
//
//
//        StudyFeedList feedList = new FeedList();
//        feedList.setId(id);
//
//        FeedLike feedLike = new FeedLike();
//        feedLike.setUser(user.get());
//        feedLike.setFeedlist(feedList);
//        feedLike.setUserkey(user.get().getId());
//        feedLikeRepository.save(feedLike);
//
//        //Optional<FeedList> feed = studyFeedListRepository.findById(id);
//        Optional<FeedList> feed = studyFeedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());
//
////		feed.ifPresent(selectList -> {
////			selectList.setTotallike(feed.get().getTotallike()+1);
////			feedListRepository.save(selectList);
////		});
//        return feed;
//    }
//
//    public Optional<StudyFeedList> dislikefeed(Optional<User> user, long id) {
//        StudyFeedList feedlist = new FeedList();
//        feedlist.setId(id);
//
//        long feedlikeno = feedLikeRepository.findlikeno(user.get(), feedlist);
//        feedLikeRepository.deleteById(feedlikeno);
//
//        Optional<FeedList> feed = studyFeedListRepository.findById(id);
////		feed.ifPresent(selectList -> {
////			selectList.setTotallike(feed.get().getTotallike()-1);
////			feedListRepository.save(selectList);
////		});
//        return feed;
//    }
//
//    public Optional<StudyFeedList> selectOne(long feedid) {
//        return studyFeedListRepository.findById(feedid);
//    }
//
//    public List<FeedLike> selectFeedlikelist(long id) {
//        List<FeedLike> feedlike = feedLikeRepository.findByfeedlistId(id);
//        return feedlike;
//    }
//
//    public StudyFeedList listfeedDetail(long id) {
//        return studyFeedListRepository.findById(id).get();
//    }
//
//    public Page<StudyFeedList> mylistfeed(Pageable pageable, long user_id) {
//        return studyFeedListRepository.findAllByuser_id(pageable, user_id);
//    }
//
//    public List<StudyFeedList> mylistfeedBydate(long user_id, LocalDateTime startdate, LocalDateTime enddate) {
//        return studyFeedListRepository.findAllByuserIdAndUpdatedAtBetween(user_id,startdate,enddate);
//    }
//
//    public Optional<StudyFeedList> mylistfeedBydateOne( long user_id, LocalDateTime startdate,LocalDateTime enddate) {
//        return studyFeedListRepository.findTop1ByuserIdAndUpdatedAtBetweenOrderByUpdatedAtDesc(user_id,startdate,enddate);
//    }
//
//    public Page<StudyFeedList> otherlistfeed(Pageable pageable, long user_id) {
//        return studyFeedListRepository.findAllByuser_idAndDeleteyn(pageable, user_id,'N');
//    }
//
    public void extractHashTag(String content,StudyFeedList feedList) {//해시태그 검출을 위한 함수
        Pattern p = Pattern.compile("\\#([0-9a-zA-Z가-힣]*)");
        Matcher m = p.matcher(content);
        String extractHashTag = null;
        while (m.find()) {
            extractHashTag = sepecialCharacter_replace(m.group());
            if (extractHashTag != null) //해시태그 저장
            {
                StudyTag tag = new StudyTag();
                Optional <StudyTag> tagResult = studyTagRepository.findByname(extractHashTag);//태그 존재 여부 확인
                if (!tagResult.isPresent())//현재 존재하지 않는 태그는 새로 저장
                {
                    tag.setName(extractHashTag);
                    studyTagRepository.save(tag);
                }
                else //현재 존재하는 태그인 경우-> FeedTag에만 저장
                    tag=tagResult.get();
                //FeedTag에 저장
                StudyFeedTag feedTag=new StudyFeedTag();
                feedTag.setStudyFeedList(feedList);
                feedTag.setStudytag(tag);
                studyFeedTagRepository.save(feedTag);
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
//
//    public Page<StudyFeedList> feedMylike(Pageable pageable, Optional<User> user, List UserId) {
//        return studyFeedListRepository.findDistinctAllByUserIdNotInAndDeleteynOrFeedlikeUserIdAndFeedlikeUserIdIsNullAndFeedlikeUserIdIsNotNullOrderByIdDesc(pageable, UserId ,'N',user.get().getId());
//    }
//
//    public Optional<StudyFeedList> listMyFeedLikeFeedDetail(long id, Optional<User> user) {
//        Optional<StudyFeedList> existsMyFeedlike= studyFeedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());
//
//        if(existsMyFeedlike.isPresent()) {
//            return studyFeedListRepository.findByIdAndFeedlikeUserId(id,user.get().getId());
//        }else {
//            return studyFeedListRepository.findById(id);
//        }
//    }
//
//    public Page<StudyFeedList> feedMylikeNotBlock(Pageable pageable, Optional<User> user) {
//        return studyFeedListRepository.findDistinctAllByDeleteynOrFeedlikeUserIdAndFeedlikeUserIdIsNullAndFeedlikeUserIdIsNotNullOrderByIdDesc(pageable,'N',user.get().getId());
//    }
//
//    public List<StudyFeedList> FindFeedUser(User user) {
//        return studyFeedListRepository.findByUser(user);
//    }
//
//    public String getenddate(String date)
//    {
//
//        int year= Integer.parseInt(date.substring(0,4));
//        int month=0;
//        if(date.charAt(4)=='0')//10보다 작은 경우
//            month=date.charAt(5)-'0';
//        else
//            month=Integer.parseInt(date.substring(4,6));
//        Calendar cal = Calendar.getInstance();
//        cal.set(year,month-1,1);
//        int endday=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        LocalDateTime localDateTime= LocalDateTime.of(year,month,endday,23,59,59);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String reuslt= localDateTime.format(formatter);
//        return reuslt;
//    }
}
