package study.share.com.source.message.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoListReq {
	
	private String todoContent;
	
	private boolean checked;
	
	private String savedDate;
	
	private String color; //색상
	
	private String name; //과목이름
}
