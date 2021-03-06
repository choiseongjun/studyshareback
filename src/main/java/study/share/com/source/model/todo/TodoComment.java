package study.share.com.source.model.todo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "todocomment")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false,exclude = {"todoDate"})
public class TodoComment extends DateAudit{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"user","feedlike","follow","todolist"})
	private User user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "tododate_id",referencedColumnName = "id")
	@JsonIgnore
	private TodoDate todoDate;
	@Column(name = "title")
	private String title;
	@Column(name = "content")
	private String content;
	
}
