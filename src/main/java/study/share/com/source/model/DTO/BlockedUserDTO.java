package study.share.com.source.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.BlockedUser;

@Data
@NoArgsConstructor
public class BlockedUserDTO {

    private long id;

    private long userId;
    private long blockedUserId;


    public BlockedUserDTO (BlockedUser blockedUser)
    {
        this.id=blockedUser.getId();
        this.userId=blockedUser.getUser().getId();
        this.blockedUserId=blockedUser.getBlockedUser().getId();
    }
}

