package study.share.com.source.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auth_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String userid;
}
