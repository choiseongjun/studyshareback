package study.share.com.source.model;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "user_external_account", uniqueConstraints = {@UniqueConstraint(columnNames = {
        "account_type", "account_id"
})})
@Getter
@Setter
@IdClass(ExternalAccountPK.class)
public class ExternalAccount extends DateAudit {

    @Id
    @Column(name="user_id")
    private String userId;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="account_type")
    private AccountType accountType;

    @Column(name="account_id")
    private String accountId;
}


