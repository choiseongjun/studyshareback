package study.share.com.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.TodoDate;
import study.share.com.source.model.User;

public interface TodoDateRepository extends JpaRepository<TodoDate, Long>{

	boolean existsBySavedDate(String savedDate);

	boolean existsBySavedDateAndUser(String savedDate, User user);

	TodoDate findBySavedDateAndUser(String savedDate, User user);

}
