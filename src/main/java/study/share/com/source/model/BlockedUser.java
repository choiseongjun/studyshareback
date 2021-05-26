package study.share.com.source.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.DTO.ReportDTO;

import javax.persistence.*;

@Entity
@Table(name = "blocked_user")
@Getter
@Setter
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "blocked_user_id")
    private User blockedUser;


}
