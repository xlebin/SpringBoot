package com.example.ssy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

    @GetMapping("/hi")
    public String niceToMeetYou(Model model){
        model.addAttribute("username","서영");
        return "greetings";
    }

    @GetMapping("/bye")                //URL 요청 접수
    public String seeYouNext(Model model){        //메서드 작성
        model.addAttribute("nickname","홍길동");   //모델 변수 등록하기
        return "goodbye";              //goodbye.mustache 반환
    }
}
