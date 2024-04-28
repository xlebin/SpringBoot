package com.example.ssy.controller;

import com.example.ssy.dto.ArticleForm;
import com.example.ssy.entity.Article;
import com.example.ssy.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j  //로깅 기능을 위한 어노테이션 추가
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @GetMapping("/articles/new")
    public String newArticleForm(){
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){ //폼 데이터를 DTO로 받기
        log.info(form.toString());
        //System.out.println(form.toString());       //DTO에 폼 데이터가 잘 담겼는지 확인

        //1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        log.info(article.toString());
        //System.out.println(article.toString()); //DTO가 엔티티로 잘 변환되는지 확인 출력

        //2. 리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        //System.out.println(saved.toString()); //article이 DB에 잘 저장되는지 확인 출력
        return "redirect:/articles/" + saved.getId();
    }



    @GetMapping("/articles/{id}")   //데이터 조회 요청 접수
    public String show(@PathVariable Long id, Model model) { //매개변수로 id 받아오기 //@PathVariable은 URL 요청으로 들어온 전달값을 컨트롤러의 매개변수로 가져오는 어노테이션
        log.info("id = " + id);     //id를 잘 받았는지 확인하는 로그 찍기

        //1. id를 조회해 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);

        //3. 뷰 페이지 반환하기
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
        //1.모든 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();
        //2.모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);
        //3.뷰 페이지 설정하기
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")  //해당 URL 요청을 접수 받으면
    public String edit(@PathVariable Long id,Model model){

        //1. 수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        //3. 뷰 페이지 설정하기
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());
        // 1. DTO를 엔티티로 변환하기
        Article articleEntity = form.toEntity();    //DTO(form)을 엔티티로 변환하기
        log.info(articleEntity.toString());         //엔티티로 잘 변환됐는지 로그 찍기
        // 2. 엔티티를 DB에 저장하기
        // 2-1. DB에서 기존 데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        // 2-2. 기존 데이터 값을 갱신하기
        if (target != null) {
            articleRepository.save(articleEntity); //엔티티를 DB에 저장(갱신)
        }
        // 3. 수정 결과 페이지로 리다이렉트하기
        return "redirect:/articles/" + articleEntity.getId() ;
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id , RedirectAttributes rttr){
        log.info("삭제 요청이 들어왔습니다!");
        //1. 삭제할 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
        //2. 대상 엔티티 삭제하기
        if(target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제됐습니다!");
        }
        //3. 결과 페이지로 리다이렉트하기
        return "redirect:/articles";
    }
}
