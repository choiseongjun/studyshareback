package study.share.com.source.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.DTO.ReportDTO;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "blocked_user")
@Getter
@Setter
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "blocked_user_id")
	@JsonIgnoreProperties({"roles", "feedlike","follow","todolist"})
    private User blockedUser;


}
