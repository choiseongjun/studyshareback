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
	
	public TodoDateResponse(TodoDate todoDate) {
		this.setId(todoDate.getId());
		this.setSavedDate(todoDate.getSavedDate());     
		this.setTodoComment(todoDate.getTodoComment());  
		this.setTodoList(todoDate.getTodoDate());    
		this.setAllRatioCnt(todoDate.getTodoDate().size()); 
		for(int i=0;i<todoDate.getTodoDate().size();i++) {
			if(todoDate.getTodoDate().get(i).getChecked()=='C') {
				completeRatioCnt++;
			}
			 
		}
	}

}
