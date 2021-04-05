package study.share.com.source.message.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoListReq {
	

	private String todoTitle;
	
	private String todoContent;
	
	private char checked;
	
	private String savedDate;
	
	private String startTime;
	
	private String endTime;
	
}
