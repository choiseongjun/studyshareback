package study.share.com.source.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "user_history")
@Getter
@Setter
public class UserHistory extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name="login_date")
    private String logindate;
    @Column(name="os_type")
    private String ostype;
    @Column(name="access_name")
    private String accessname;
    @Column(name="access_path")
    private String accesspath;
    @Column(name="ipaddress")
    private String ipaddress;
    @Column(name="session_lastaccess")
    private String sessionlastaccess;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
