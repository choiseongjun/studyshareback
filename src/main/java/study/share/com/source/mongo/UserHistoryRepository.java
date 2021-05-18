package study.share.com.source.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import study.share.com.source.model.UserHistory;

public interface UserHistoryRepository extends MongoRepository<UserHistory, String> {



}
