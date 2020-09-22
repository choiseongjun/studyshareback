package study.share.com.source.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.model.UploadFile;
import study.share.com.source.model.DTO.ImagesDTO;
import study.share.com.source.repository.UploadFileRepository;

@Service
public class UploadFileService {

	@Autowired
	UploadFileRepository uploadfileRepository;
	@Autowired
	S3Service s3Service;
//	public void saveFileList(List<MultipartFile> file) {
//		for (MultipartFile filelists: file) {
//			UploadFile uploadFile = new UploadFile();
//			UUID uid2 = UUID.randomUUID();//랜덤uid2
//			String originalFileName2 = filelists.getOriginalFilename();
//			String fileExtension2 = originalFileName2.substring(originalFileName2.lastIndexOf(".") + 1).toLowerCase();
//			//String imageNAME2 = filelists.getName();
//			String savedName2 = uid2.toString();// 랜덤아이디
//			
//			uploadFile.setImageExtension(fileExtension2);
//			uploadFile.setFile_name(savedName2);
//			uploadFile.setReal_name(originalFileName2);
//			uploadFile.setFile_path(webImagePath);
//		}
//	}

	public void saveFileList(MultipartFile filelists, String imgPath) {
		UploadFile uploadFile = new UploadFile();
		UUID uid2 = UUID.randomUUID();//랜덤uid2
		String originalFileName2 = filelists.getOriginalFilename();
		String fileExtension2 = originalFileName2.substring(originalFileName2.lastIndexOf(".") + 1).toLowerCase();
		//String imageNAME2 = filelists.getName();
		String savedName2 = uid2.toString();// 랜덤아이디

		uploadFile.setImageExtension(fileExtension2);
		uploadFile.setFilename(savedName2);
		uploadFile.setRealname(originalFileName2);
		uploadFile.setFilepath(imgPath);
		uploadFile.setFilesize(filelists.getSize());
		
		uploadfileRepository.save(uploadFile);
		
	}

	public List<ImagesDTO> getList(String imgPath) {
		List<UploadFile> uploadFilelist = uploadfileRepository.findByFilepath(imgPath);
	    List<ImagesDTO> galleryDtoList = new ArrayList<>();

	    for (UploadFile Images : uploadFilelist) {
	        galleryDtoList.add(convertEntityToDto(Images));
	    }

	    return galleryDtoList;
	}
	private ImagesDTO convertEntityToDto(UploadFile uploadfile) {
	    return ImagesDTO.builder()
	            .id(uploadfile.getId())
	            .filePath(uploadfile.getFilepath())
	            .imgFullPath("https://" + s3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + uploadfile.getFilename())
	            .build();
	}
}
