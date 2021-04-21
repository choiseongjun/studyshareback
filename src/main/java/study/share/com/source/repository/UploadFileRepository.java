package study.share.com.source.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import study.share.com.source.model.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long>{

	UploadFile findBySrc(String imgPath);

	List<UploadFile> findByFeedlistId(long id); 

}
