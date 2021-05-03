package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.AlarmHistory;
import study.share.com.source.model.User;

import javax.persistence.*;

@Getter
@Setter
@Data
@NoArgsConstructor
public class AlarmHistoryDTO {

    private long id;
    private long function;
    private String content;
    private long toUserId;
    private long fromUserId;
    private String fromUserSId;

    public AlarmHistoryDTO (AlarmHistory alarmHistory)
    {
        this.function=alarmHistory.getFunction();
        this.id=alarmHistory.getId();
        this.content=alarmHistory.getContent();
        this.toUserId=alarmHistory.getToUser().getId();
        this.fromUserId=alarmHistory.getFromUser().getId();
        this.fromUserSId =alarmHistory.getFromUser().getUserid();
    }
}
