package study.share.com.source.model.DTO.study;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.*;
import study.share.com.source.model.study.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
@Data
@NoArgsConstructor
public class StudyFeedListDTO {

    private long id;

    private String content;

    private long totallike;

    private String nickname;

    private long userKey;

    private long groupid;

    private UserProfileImage userProfileImage;

    private List<StudyUploadFile> uploadfile;

    private List<StudyFeedLike> feedlike;

    private Stream<StudyFeedLike> myFeedlike;


    private List<StudyFeedReply> feedreply=new ArrayList<StudyFeedReply>();

    private List<StudyFeedTag> feedTagList =new ArrayList<>();

    private List<StudyTag> tagList= new ArrayList<>();

    private long feedreplysize;
    private LocalDateTime createdAt;

    private List<Follow> follwer = new ArrayList<Follow>();



    public StudyFeedListDTO(StudyFeedList feedlist) {
        this.setId(feedlist.getId());
        this.setUserKey(feedlist.getUser().getId());
        this.setContent(feedlist.getContent());
        this.setTotallike(feedlist.getStudyfeedlike().size());
        this.setUploadfile(feedlist.getUploadfile());
        this.setNickname(feedlist.getUser().getNickname());
        this.setUserProfileImage(feedlist.getUser().getUserProfileImage());
        this.setFeedreplysize(feedlist.getStudyfeedreply().stream().filter(t->t.getDeleteyn()=='N').count());
        this.setFeedlike(feedlist.getStudyfeedlike());
        this.setFeedreply(feedreply);
        this.setCreatedAt(feedlist.getCreatedAt());
    }

    public StudyFeedListDTO(StudyFeedList feedlist, User user) {

        this.setId(feedlist.getId());
        this.setUserKey(feedlist.getUser().getId());
        this.setContent(feedlist.getContent());
        this.setTotallike(feedlist.getStudyfeedlike().size());
        this.setUploadfile(feedlist.getUploadfile());
        this.setNickname(feedlist.getUser().getNickname());
        this.setUserProfileImage(feedlist.getUser().getUserProfileImage());
        this.setFeedreplysize(feedlist.getStudyfeedreply().stream().filter(t->t.getDeleteyn()=='N').count());
        this.setFeedlike(feedlist.getStudyfeedlike());
        this.setMyFeedlike(feedlist.getStudyfeedlike().stream().filter(t->t.getUser().getId()==user.getId()));
        this.setFeedreply(feedreply);
        this.setCreatedAt(feedlist.getCreatedAt());
        this.setFollwer(user.getFollow());
    }

}
