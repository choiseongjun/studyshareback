package study.share.com.source.model.DTO;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.Subject;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;

@Getter
@Setter
public class TodoListDTO {
	private long id;
	
	private String todoContent;
	
	private String highlighter;//형광펜
	
	private boolean checked;
	
	private String savedDate;
	
	private User user;
	private Subject subject;
	
	public boolean isChecked(){return this.checked;}

	public void setChecked(boolean active){this.checked = active;}
	
	private long completetodo;
	
	private long uncompletetodo;
	
	private long percompletetodo;
	
	public TodoListDTO(TodoList todoList) {
		this.setTodoContent(todoList.getTodoContent());
		this.setHighlighter(todoList.getHighlighter());
		this.setSavedDate(todoList.getSavedDate());
		this.setSubject(todoList.getSubject());
		this.setUser(todoList.getUser());
		this.setId(todoList.getId());
		this.setChecked(todoList.isChecked());
	}

	public TodoListDTO(long completeTodo2, long uncompleteTodo2, long perCompleteTodo) {
		this.setCompletetodo(completeTodo2);
		this.setUncompletetodo(uncompleteTodo2);
		this.setPercompletetodo(perCompleteTodo);
	}

}
