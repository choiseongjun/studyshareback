package study.share.com.source.model.report;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.FeedReply;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "reportfeedreply")
@Getter
@Setter 
@NoArgsConstructor
public class ReportFeedReply extends DateAudit{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "feedreply_id")
	@JsonIgnore
	private FeedReply feedreply;
	@ManyToOne(optional = false)  
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"feedlike","todolist","follow","roles"})	
	private User user;

	@Column(name="content")
	private String content;
	@ManyToOne(optional = false)
	@JoinColumn(name = "reporter")
	@JsonIgnoreProperties({"feedlike","todolist","follow","roles"})
	private User reporter;
}
