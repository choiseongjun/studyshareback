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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tododate")
@Getter
@Setter
@NoArgsConstructor
public class TodoDate{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"user","feedlike","follow","todolist"})
	private User user;
	@OneToMany(orphanRemoval=true,mappedBy = "todoDate")
	private List<TodoList> todoDate=new ArrayList<TodoList>();
	
	@OneToOne(mappedBy = "todoDate",fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"tododate","user"})
    private TodoComment todoComment;


	@Column(name = "saveddate")
	private String savedDate;
}
