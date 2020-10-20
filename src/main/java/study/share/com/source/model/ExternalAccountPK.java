package study.share.com.source.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ExternalAccountPK implements Serializable {
    private AccountType accountType;
    private String userId;

}
