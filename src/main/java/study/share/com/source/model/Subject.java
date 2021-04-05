//\package study.share.com.source.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
//import lombok.Getter;
//import lombok.Setter;
//import study.share.com.source.model.common.DateAudit;
//
//@Entity
//@Table(name = "subject")
//@Getter
//@Setter
//public class Subject extends DateAudit{
//
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long id;
//	
//	private String name;
//	
//	@ManyToOne(optional = false)
//	@JoinColumn(name = "color_id")
//	private Color color;
//	@OneToMany(orphanRemoval=true,mappedBy = "subject")
//	private List<TodoList> todolist=new ArrayList<TodoList>();
//	
//}
