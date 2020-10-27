package study.share.com.source.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@JoinColumn(name = "userId")
	@JsonIgnoreProperties({"feedlike"})	
	private User user;
	
	private long userkey;
	
}
