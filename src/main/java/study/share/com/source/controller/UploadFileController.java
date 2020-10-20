package study.share.com.source.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.config.BASE64DecodedMultipartFile;
//import study.share.com.source.model.DTO.MobileImage;
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
//		List<ImagesDTO> Images = new ArrayList<>();
		List<String> Images = new ArrayList<>();
		for (MultipartFile filelists: file) {
				imgPath = s3Service.upload(filelists);
				uploadFileService.saveFileList(filelists,imgPath);
				//Images.addAll(uploadFileService.getList(imgPath));
				Images.add(imgPath);
		}
		
		return ResponseEntity.ok(Images);
	}
//	@PostMapping("/mobile/feed/upload")
//	public ResponseEntity<?> mobilefeedupload(@RequestParam(name="fileName",required = false) String fileName
//											,@RequestParam(name="type",required = false) String type
//											,@RequestParam(name="base",required = false) String base
//											,@RequestParam(name="fileSize",required = false) long fileSize) throws IOException {
//		
//		String imgPath="";
//		List<String> Images = new ArrayList<>(); 
//	
//		String[] strings = base.split(",");
//		String extension;
//        switch (strings[0]) {//check image's extension
//            case "data:image/jpeg;base64":
//                extension = "jpeg";
//                break;
//            case "data:image/png;base64":
//                extension = "png";
//                break;
//            default://should write cases for more images types
//                extension = "jpg";
//                break;
//        }
//        //convert base64 string to binary data
//        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
//		BASE64DecodedMultipartFile multi = new BASE64DecodedMultipartFile(data,fileName,type,fileSize);
//
//		imgPath = s3Service.mobileupload(multi);
//		uploadFileService.saveFileList(multi,imgPath);
//		Images.add(imgPath);
//		System.out.println(Images);
//		return ResponseEntity.ok(Images);
//	}
	@PostMapping("/mobile/feed/upload")
	public ResponseEntity<?> mobilefeedupload(@RequestParam(name="fileName",required = false) List<String> fileName
											,@RequestParam(name="fileSize",required = false) List<Long> fileSize
											,@RequestParam(name="type",required = false) List<String> type
											,@RequestParam(name="base",required = false) List<String> base) throws IOException{
		
		
		List<String> Images = new ArrayList<>(); 
		if(fileName.size()==1) {//이미지 1개만 넘겼을떄..
			String imgPath="";
			String basevalue= base.get(0)+","+base.get(1);//이유를 모르겠지만 1개만  넘어왔을땐 쪼개져서 넘어온다..
			String[] strings = basevalue.split(",");

			String extension;
	        switch (strings[0]) {//check image's extension
	            case "data:image/jpeg;base64":
	                extension = "jpeg";
	                break;
	            case "data:image/png;base64":
	                extension = "png";
	                break;
	            default://should write cases for more images types
	                extension = "jpg";
	                break;
	        }
	        //convert base64 string to binary data
	        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
	        System.out.println(data);
			BASE64DecodedMultipartFile multi = new BASE64DecodedMultipartFile(data,fileName.get(0),type.get(0),fileSize.get(0));

			imgPath = s3Service.mobileupload(multi);
			uploadFileService.saveFileList(multi,imgPath);
			Images.add(imgPath);
		}else {
			for(int i=0;i<fileName.size();i++) {
				System.out.println("i value=="+base.get(i));
				String imgPath="";
				
				String[] strings = base.get(i).split(",");
				String extension;
		        switch (strings[0]) {//check image's extension
		            case "data:image/jpeg;base64":
		                extension = "jpeg";
		                break;
		            case "data:image/png;base64":
		                extension = "png";
		                break;
		            default://should write cases for more images types
		                extension = "jpg";
		                break;
		        }
		        //convert base64 string to binary data
		        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
				BASE64DecodedMultipartFile multi = new BASE64DecodedMultipartFile(data,fileName.get(i),type.get(i),fileSize.get(i));

				imgPath = s3Service.mobileupload(multi);
				uploadFileService.saveFileList(multi,imgPath);
				Images.add(imgPath);
			}
		}
		
		return ResponseEntity.ok(Images);
	}
	 
}
