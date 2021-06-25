package study.share.com.source.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.studygroup.StudyUploadFile;

import java.util.List;

public interface StudyUploadFileRepository extends JpaRepository<StudyUploadFile,Long> {

    StudyUploadFile findBySrc(String imgPath);

    List<StudyUploadFile> findByStudyFeedListId(long id);
}
