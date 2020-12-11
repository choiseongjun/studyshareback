package study.share.com.source.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "verification_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_no")
    private Long userNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "token")
    private String token;

    @Column(name = "used_yn")
    private boolean usedYn;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expriesAt;

    public boolean isExpired() {
        if(expriesAt.isAfter(LocalDateTime.now())) {
            return true;
        } else {
            return false;
        }
    }
}
