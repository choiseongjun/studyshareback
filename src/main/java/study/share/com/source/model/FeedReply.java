package study.share.com.source.model;

import java.util.ArrayList;
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
import org.hibernate.annotations.ColumnDefault;
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
	@ManyToOne(optional = false)
	@JsonIgnoreProperties({"feedlike","follow","todolist","userProfileImage"})
	@JoinColumn(name = "user_id")
	private User user;
	@Transient//댓글 추가할때 상태변화로 바로 넣을수있도록 임시조치
	private long feedlistkey;

	@Column(name = "origin_no")
	@ColumnDefault("0")
	private long origin_no;

	@Column(name = "group_ord")
	@ColumnDefault("0")
	private long group_ord;
	
	@OneToMany(orphanRemoval=true,mappedBy = "feedReply")
	private List<FeedReplyLike> feedReplylike=new ArrayList<FeedReplyLike>();

}
