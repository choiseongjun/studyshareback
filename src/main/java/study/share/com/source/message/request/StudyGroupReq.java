package study.share.com.source.message.request;


import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.User;

import javax.persistence.*;

@Getter
@Setter
public class StudyGroupReq {

    private long memberLimit;
    private String title;
    private String introduce;
    private boolean recruitStatus;
    private boolean joinVerified;
}
