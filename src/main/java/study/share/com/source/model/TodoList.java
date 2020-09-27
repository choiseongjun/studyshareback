package study.share.com.source.model;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	private String fontColor;
	
	@Convert(converter=BooleanToYNConverter.class)
	private boolean checked;
	
	private String savedDate;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
}
