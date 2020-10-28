package study.share.com.source.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "follow")
@Getter
@Setter
@NoArgsConstructor
public class Follow {

	// fromUser가 toUser를 following 함.
	// toUser를 fromUser가 follower 함.
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name="fromUserId")
	@JsonIgnoreProperties({"roles", "feedlike","follow"})
	private User fromUser;
	
	@ManyToOne
	@JsonIgnoreProperties({"roles", "feedlike","follow"})
	@JoinColumn(name="toUserId")
	private User toUser;
	
	
}
