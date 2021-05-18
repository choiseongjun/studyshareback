package study.share.com.source.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

//@Table(name = "user_history")
@Document(collection = "user_history")
@Getter
@Setter
public class UserHistory extends DateAudit {

    @Id
    private String id;
    private String logindate;
    private String ostype;
    private String accessname;
    private String accesspath;
    private String ipaddress;
    private String sessionlastaccess;
    private long userNumber;
    private String userId;
    
}
