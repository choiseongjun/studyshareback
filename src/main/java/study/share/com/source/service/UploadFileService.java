package study.share.com.source.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.model.UploadFile;
import study.share.com.source.repository.feed.UploadFileRepository;

@Service
public class UploadFileService {

	@Autowired
	UploadFileRepository uploadfileRepository;
	@Autowired
	S3Service s3Service;
	public void saveFileList(MultipartFile filelists, String imgPath) {
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy-MM-dd");
				
		Date time = new Date();
				
		String filesavedtime = format1.format(time);
		
		UploadFile uploadFile = new UploadFile();
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
		
		uploadfileRepository.save(uploadFile);
		
	}
	public void deleteFeedListId(String src) {
		// TODO Auto-generated method stub
		UploadFile file = uploadfileRepository.findBySrc(src);
		file.setFeedlist(null);
		uploadfileRepository.save(file);
	}

//	public List<ImagesDTO> getList(String imgPath) {
//		List<UploadFile> uploadFilelist = uploadfileRepository.findByFilepath(imgPath);
//	    List<ImagesDTO> galleryDtoList = new ArrayList<>();
//
//	    for (UploadFile Images : uploadFilelist) {
//	        galleryDtoList.add(convertEntityToDto(Images));
//	    }
//
//	    return galleryDtoList;
//	}
//	private ImagesDTO convertEntityToDto(UploadFile uploadfile) {
//	    return ImagesDTO.builder()
//	            .id(uploadfile.getId())
//	            .filePath(uploadfile.getFilepath())
//	            .imgFullPath("https://" + s3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadfile.getFilename())
//	            .build();
//	}
}
