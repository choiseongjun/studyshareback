package study.share.com.source.controller;

import io.swagger.annotations.ApiOperation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ViewController {

    @ApiOperation(value="비밀번호 변경 페이지 실행",notes="비밀번호 변경 페이지 실행")
    @RequestMapping(value="passwordset")
    public String changePWpage(Model model) throws IOException {
        return "passwordset";
    }
}
