package study.share.com.source.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import study.share.com.source.message.request.Message;

@RestController
public class FeedListController {

	@PostMapping("/feed")
	public ResponseEntity<?> feed(@RequestPart(name = "images", required = false) List<Message> file
			,@RequestPart(name = "content", required = false) String content) throws IOException {
		System.out.println(file);
//		for(int i=0;i<file.size();i++) {
//			System.out.println(file.get(i).toString());
//		}
//		for (MultipartFile filelists: file) {
//			System.out.println(filelists.getName());
//			System.out.println(filelists.getOr iginalFilename());
//		}
		System.out.println(content);
		return null;
	}
}
