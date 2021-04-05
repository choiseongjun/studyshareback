package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.share.com.source.message.request.TodoListReq;
import study.share.com.source.model.DTO.ReportDTO;
import study.share.com.source.model.Report;
import study.share.com.source.model.TodoDate;
import study.share.com.source.model.TodoList;
import study.share.com.source.model.User;
import study.share.com.source.repository.UserRepository;
import study.share.com.source.service.FeedListService;
import study.share.com.source.service.ReportService;
import study.share.com.source.service.UserService;

import java.security.Principal;
import java.util.Optional;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @ApiOperation(value="불편사항 접수",notes="불편사항 접수")
    @PostMapping(path="/report/new")
    public ResponseEntity<?> reportwrite(@RequestBody ReportDTO reportDTO) {
        try {
            Report report=reportService.saveReport(reportDTO);
            return new ResponseEntity<>(report,HttpStatus.OK);
        }catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
        }
    }

//    @ApiOperation(value="불편사항 수정",notes="불편사항 수정")
//    @PostMapping(path="/report/modfiy/{id}")
//    public ResponseEntity<?> reportmodify(@PathVariable long id, @RequestBody ReportDTO reportDTO) {
//        try {
//            Report report=reportService.modfiyReport(reportDTO);
//            return new ResponseEntity<>(report,HttpStatus.OK);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("실패하였습니다.새로고침후 다시 시도해주세요",HttpStatus.BAD_REQUEST);
//        }
//    }



}
