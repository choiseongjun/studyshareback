package study.share.com.source.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
import study.share.com.source.model.UserHistory;

@Repository
public interface UserHistoryRepository extends MongoRepository<UserHistory, String> {



}
