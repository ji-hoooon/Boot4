package com.movie.boot4.service;


import com.movie.boot4.dto.ReviewDTO;
import com.movie.boot4.entity.Member;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;

import java.util.List;

public interface ReviewService {

    //특정 영화의 모든 리뷰를 가저온다.
    List<ReviewDTO> getListOfMovie(Long mno);

    //영화 리뷰를 추가
    Long register(ReviewDTO movieReviewDTO);

    //특정한 영화리뷰 수정
    void modify(ReviewDTO movieReviewDTO);

    //영화 리뷰 삭제
    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewDTO movieReviewDTO){
        //영화 객체와 멤버 객체가 필요하다.
        //해당 영화객체는 영화리뷰DTO에서 mno을 이용해 객체 생성
        //해당 회원객체는 영화리뷰DTO에서 mid를 이용해 객체 생성
        Review movieReview = Review.builder()
                .reviewnum(movieReviewDTO.getReviewnum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();
        return movieReview;
    }

    default ReviewDTO entityToDTO(Review review){
        ReviewDTO reviewDTO= ReviewDTO.builder()
                .reviewnum(review.getReviewnum())
                .mno(review.getMovie().getMno())
                .mid(review.getMember().getMid())
                .nickname(review.getMember().getNickname())
                .email(review.getMember().getEmail())
                .grade(review.getGrade())
                .text(review.getText())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();

        return reviewDTO;
    }

}
