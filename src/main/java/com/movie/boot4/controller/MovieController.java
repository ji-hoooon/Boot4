package com.movie.boot4.controller;

import com.movie.boot4.dto.MovieDTO;
import com.movie.boot4.dto.PageRequestDTO;
import com.movie.boot4.service.MovieService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
//final 객체 의존성 주입 어노테이션
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/register")
    public void register(){

    }

    //전달된 파라미터를 MovieDTO로 수집해 MovieService 타입 객체의 등록메서드 호출
    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes){
        log.info("movieDTO : "+movieDTO );
        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg",mno);
        //리다이렉트 시에만 데이터 전달
        //addAttribute는 GET 방식이며 페이지를 새로고침 한다 해도 값이 유지된다.
        //addFlashAttribute는 POST 방식이며 이름처럼 일회성 데이터라 새로고침 하면 값이 사라진다.

        return "redirect:/movie/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("pageReQuestDTO : "+pageRequestDTO);

        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    //조회와 수정에서 사용하는 URL 처리
    @GetMapping({"/read", "/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("mno :"+ mno);
        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto", movieDTO);
    }
}
