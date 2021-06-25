package study.share.com.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.share.com.source.model.UploadFile;
import study.share.com.source.model.studygroup.StudyUploadFile;
import study.share.com.source.repository.UploadFileRepository;
import study.share.com.source.repository.study.StudyUploadFileRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class StudyUploadFileService {

    @Autowired
    StudyUploadFileRepository studyUploadFileRepository;
    @Autowired
    S3Service s3Service;
    public void saveFileList(MultipartFile filelists, String imgPath) {
        SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");

        Date time = new Date();

        String filesavedtime = format1.format(time);

        StudyUploadFile uploadFile = new  StudyUploadFile();
        UUID uid2 = UUID.randomUUID();//랜덤uid2
        String originalFileName2 = filelists.getOriginalFilename();
        String fileExtension2 = originalFileName2.substring(originalFileName2.lastIndexOf(".") + 1).toLowerCase();
        //String imageNAME2 = filelists.getName();
        String savedName2 = uid2.toString();// 랜덤아이디

        uploadFile.setImageExtension(fileExtension2);
        uploadFile.setFilename(savedName2+filesavedtime);
        uploadFile.setRealname(originalFileName2);
        uploadFile.setSrc(imgPath);
        uploadFile.setFilesize(filelists.getSize());

        studyUploadFileRepository.save(uploadFile);

    }
    public void deleteFeedListId(String src) {
        // TODO Auto-generated method stub
        StudyUploadFile file = studyUploadFileRepository.findBySrc(src);
        file.setStudyFeedList(null);
        studyUploadFileRepository.save(file);
    }

}
