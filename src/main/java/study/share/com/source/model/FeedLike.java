package study.share.com.source.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "feedlike")
@Getter
@Setter
public class FeedLike extends DateAudit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "feedlistId")
	@JsonIgnore
	private FeedList feedlist;
	@ManyToOne(optional = false)  
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"feedlike","todolist","follow","roles"})	
	private User user;
	
	private long userkey;
	@Transient
	private boolean tempFollow;
	
}
