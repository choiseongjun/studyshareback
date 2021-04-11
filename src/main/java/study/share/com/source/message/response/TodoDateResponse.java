package study.share.com.source.message.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.share.com.source.model.TodoComment;
import study.share.com.source.model.TodoDate;
import study.share.com.source.model.TodoList;
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
	
	public TodoDateResponse(TodoDate todoDate,List<TodoList> todoList2) {
		this.setId(todoDate.getId());
		this.setSavedDate(todoDate.getSavedDate());     
		this.setTodoComment(todoDate.getTodoComment());  
		this.setTodoList(todoList2);    
		this.setAllRatioCnt(todoDate.getTodoLists().size()); 
		for(int i=0;i<todoDate.getTodoLists().size();i++) {
			if(todoDate.getTodoLists().get(i).getChecked()=='C') {
				completeRatioCnt++;
			}
			 
		}
	}

}
