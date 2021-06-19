package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import study.share.com.source.model.Follow;

@Getter
@Setter
@NoArgsConstructor

public class FollowDTO {

    private Long toUserId;
    private Long fromUserId;

    public FollowDTO (Follow follow)
    {
        this.fromUserId=follow.getFromUser().getId();
        this.toUserId=follow.getToUser().getId();
    }

}

