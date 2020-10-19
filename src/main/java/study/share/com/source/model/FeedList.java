package study.share.com.source.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "feedlist")
@Getter
@Setter
public class FeedList extends DateAudit{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "content")
	private String content;
	@Column(name = "total_like")
	private long totallike;
	@Column(name = "delete_yn",columnDefinition = "char(1) default 'N'")
	private char deleteyn;
	@Column(name = "ip_address")
	private String ipaddress;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;
	@OneToMany(orphanRemoval=true,mappedBy = "feedlist")
	private List<UploadFile> uploadfile=new ArrayList<UploadFile>();
	@OneToMany(orphanRemoval=true,mappedBy = "feedlist")
	private List<FeedLike> feedlike=new ArrayList<FeedLike>();
	@OneToMany(orphanRemoval=true,mappedBy = "feedlist")
	private List<FeedReply> feedreply=new ArrayList<FeedReply>();
	@OneToMany(orphanRemoval=true,mappedBy = "feedlist")
	private List<FeedTag> feedtag;
	
}
