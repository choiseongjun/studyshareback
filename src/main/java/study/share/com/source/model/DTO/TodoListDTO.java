package study.share.com.source.model.DTO;

import lombok.Getter;
import lombok.Setter;
import study.share.com.source.model.todo.TodoList;
import study.share.com.source.model.User;

@Getter
@Setter
public class TodoListDTO {
	private long id;
	
	private String todoTitle;
	
	private String todoContent;
	
	private char checked;
	
	private String savedDate;
	
	private String startTime;
	
	private String endTime;
	
	private User user;
	
	private long Completeresult;
	
	private long resultAll;
	
	
	public TodoListDTO(TodoList todoList,User user,long Completeresult,long resultAll) {
		this.setTodoTitle(todoList.getTodoTitle());
		this.setTodoContent(todoList.getTodoContent());
		this.setChecked(todoList.getChecked());
		this.setStartTime(todoList.getStartTime());
		this.setEndTime(todoList.getEndTime());
//		this.setUser(user);
		this.setCompleteresult(Completeresult);
		this.setResultAll(resultAll);
	}
}
