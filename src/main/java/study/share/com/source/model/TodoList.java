package study.share.com.source.model;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.share.com.source.model.common.DateAudit;

@Entity
@Table(name = "todolist")
@Getter
@Setter
@NoArgsConstructor
public class TodoList extends DateAudit{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String todoContent;
	
	private String highlighter;//형광펜
	
	@Convert(converter=BooleanToYNConverter.class)
	private boolean checked;
	
	private String savedDate;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"user","feedlike","follow","todolist"})
	private User user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "subject_id")
	@JsonIgnoreProperties({"todolist","color"})
	private Subject subject;
	
	@Transient
	private long completetodo;
	@Transient
	private long uncompletetodo;
	
	public boolean isChecked(){return this.checked;}

	public void setChecked(boolean active){this.checked = active;}
}
