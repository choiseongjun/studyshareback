package study.share.com.source.model.todo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.User;
import study.share.com.source.model.common.DateAudit;
import study.share.com.source.model.todo.TodoDate;

@Entity
@Table(name = "todolist")
@Getter
@Setter
@NoArgsConstructor
public class TodoList extends DateAudit{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "tododate_id")
	@JsonIgnoreProperties({"user","todoLists","todoComment"})
	private TodoDate todoLists;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"feedlike","follow","todolist"})
	private User user;
	@Column(name = "todotitle")
	private String todoTitle;
 
	@Column(name = "todocontent")
	private String todoContent;
	
	@Column(name = "checked")
	private char checked;
	

	@Column(name = "starttime")
	private String startTime;
	@Column(name = "endtime")
	private String endTime;
	

	
	@Transient
	private long completetodo;
	@Transient
	private long uncompletetodo;
	
}
