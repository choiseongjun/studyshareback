package study.share.com.source.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.model.DTO.ImagesDTO;
import study.share.com.source.service.S3Service;
import study.share.com.source.service.UploadFileService;

@RestController
public class UploadFileController {
	
	@Autowired
	S3Service s3Service;
	@Autowired
	UploadFileService uploadFileService;
	
	@PostMapping("/feed/upload")
	public ResponseEntity<?> feedupload(@RequestPart(name = "images", required = false) List<MultipartFile> file) throws IOException {
		String imgPath="";
		List<ImagesDTO> Images = new ArrayList<>();
		for (MultipartFile filelists: file) {
				imgPath = s3Service.upload(filelists);
				uploadFileService.saveFileList(filelists,imgPath);
				Images.addAll(uploadFileService.getList(imgPath));
		}
		 
		return ResponseEntity.ok(Images);
	}
}
