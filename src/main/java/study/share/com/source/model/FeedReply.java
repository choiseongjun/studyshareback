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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "feedreply")
@Getter
@Setter
public class FeedReply extends DateAudit{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "content")
	private String content;
	@Column(name = "delete_yn",columnDefinition = "char(1) default 'N'")
	private char deleteyn;
	@Column(name = "ip_address")
	private String ipaddress;
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
	@JoinColumn(name = "feedlist_id")
	@JsonIgnore
	private FeedList feedlist;
	@ManyToOne(optional = false,fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
