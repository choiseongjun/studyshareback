package study.share.com.source.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import study.share.com.source.message.request.TodoCommentReq;
import study.share.com.source.model.TodoComment;
import study.share.com.source.model.TodoDate;
import study.share.com.source.model.User;
import study.share.com.source.repository.TodoCommentRepository;
import study.share.com.source.repository.TodoDateRepository;

@Service
public class TodoCommentService {

	
	@Autowired
	TodoCommentRepository todoCommentRepository;
	@Autowired
	TodoDateRepository todoDateRepository;
	
	public TodoComment saveComment(User user,TodoDate todoDate, TodoCommentReq todoCommentReq) {
		
		boolean dateCheck = todoDateRepository.existsBySavedDate(todoDate.getSavedDate());
		System.out.println("dateCheck@#$@#$#$"+dateCheck);
		if(!dateCheck) {//날짜가 없다면..
			TodoDate returnTodoDate = todoDateRepository.save(todoDate);
			
			TodoComment todoComment = new TodoComment();
			todoComment.setTodoDate(returnTodoDate);
			todoComment.setUser(user);
			todoComment.setTitle(todoCommentReq.getTitle());
			todoComment.setContent(todoCommentReq.getContent());
			
			return todoCommentRepository.save(todoComment);
		}else {
			TodoDate returnTodoDate = todoDateRepository.findBySavedDate(todoDate.getSavedDate());
			
			TodoComment todoComment = new TodoComment();
			todoComment.setTodoDate(returnTodoDate);
			todoComment.setUser(user);
			todoComment.setTitle(todoCommentReq.getTitle());
			todoComment.setContent(todoCommentReq.getContent());
			
			return todoCommentRepository.save(todoComment);
		}
		
	}

	public Optional<TodoComment> updateMyTodoComment(long id, TodoCommentReq todoCommentReq) {
		Optional<TodoComment> todoComment=todoCommentRepository.findById(id);
		todoComment.ifPresent(selectList -> {
			selectList.setTitle(todoCommentReq.getTitle());
			selectList.setContent(todoCommentReq.getContent());
			todoCommentRepository.save(selectList);
		}); 
		 
		return todoCommentRepository.findById(id);
	}

	public void deleteTodoComment(long id) { 
		todoCommentRepository.deleteById(id);		
	}

}
