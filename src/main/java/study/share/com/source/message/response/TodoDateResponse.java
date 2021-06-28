package study.share.com.source.message.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.todo.TodoComment;
import study.share.com.source.model.todo.TodoDate;
import study.share.com.source.model.todo.TodoList;
import study.share.com.source.model.User;

@Data
@NoArgsConstructor
public class TodoDateResponse {

	private long id;
	private String savedDate;

	private List<TodoList> todoList=new ArrayList<TodoList>();
    private TodoComment todoComment;
	private User user;
	
	private long completeRatioCnt;
	private long allRatioCnt;
	
	public TodoDateResponse(TodoDate todoDate,List<TodoList> todoList2,TodoComment todoComment) {
		this.setId(todoDate.getId());
		this.setSavedDate(todoDate.getSavedDate());    
		System.out.println(todoComment);
		this.setTodoComment(todoComment);  
		this.setTodoList(todoList2);    
		this.setAllRatioCnt(todoList2.size()); 
		for(int i=0;i<todoList2.size();i++) {
			if(todoList2.get(i).getChecked()=='C') {
				completeRatioCnt++;
			}
			 
		}
	}

}
