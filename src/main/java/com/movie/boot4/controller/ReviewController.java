package com.movie.boot4.controller;

import com.movie.boot4.dto.ReviewDTO;
import com.movie.boot4.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//JSON 처리를 위한 RestController 설계
@RestController
//객체를 JSON으로  : RequestBody
//JSON을 객체로   : ResponseBody

//경로 처리를 위한 어노테이션
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

//    해당 영화 모든 리뷰 반환, 새로운 리뷰 등록
//    RestController에서 수정 : PutMapping 사용
//    RestController에서 삭제 : DeleteMapping 사용

//    @GetMapping("/{mno}/all")
//    @PostMapping("/{mno}")
//    @PutMapping("/{mno}/{reviewnum}")
//    @DeleteMapping("/{mno}/{reviewnum}")

    //해당 영화 모든 리뷰 반환 -> mno을 getListOfMovie 메서드에 파라미터로 전달
    @GetMapping("/{mno}/all")
    //@PathVariable은 URL 경로에 변수를 넣는다.
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno){
        log.info("-------list-----------");
        log.info("MNO : "+mno);

        //getListOfMovie : 특정 영화의 모든 리뷰를 가저오는 메서드
        List<ReviewDTO> reviewDTOlist = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOlist, HttpStatus.OK);
    }

    //새로운 리뷰 등록 후 해당 리뷰 번호 반환 -> 요청 본문에 리뷰DTO를 register메서드의 파라미터로 전달
    @PostMapping("/{mno}")
    //스프링에서의 비동기 처리를 위한 어노테이션
    //요청본문 requestBody, 응답본문 responseBody
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO reviewDTO){
        log.info("-----------add MovieReview-------------");
        log.info("reviewDTO: "+ reviewDTO);

        Long reviewnum = reviewService.register(reviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }


    //RestController에서 수정 : PutMapping 사용
    //RestController에서 삭제 : DeleteMapping 사용
    //리뷰 수정 -> 경로에 변수를 넣고, 요청 본문을 전달하는데, 리뷰DTO를 modify 메서드에 파라미터로 전달
    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum, @RequestBody ReviewDTO reviewDTO){
        log.info("----------------modify MovieReview-------------"+ reviewnum);
        log.info("reviewDTO : "+ reviewDTO);

        reviewService.modify(reviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    //리뷰 삭제 -> 경로에 변수를 넣고, 리뷰번호를 remove메서드의 파라미터로 전달
    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable Long reviewnum){
        log.info("--------------modify removeReview--------------");
        log.info("reviewnum: "+ reviewnum);

        reviewService.remove(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }
}
