package study.share.com.source.model;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "alarm_history")
@Getter
@Setter
public class AlarmHistory extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name="alarm_function")//알람 종류 구분자
    private long function;
    @Column(name="content")//알람 내용
    private String content;
    @ManyToOne
    @JoinColumn(name = "toUserId")//알람을 확인하는 사용자
    private User toUser;
    @ManyToOne
    @JoinColumn(name = "fromUserId")//알람 발생 사용자
    private User fromUser;

}
