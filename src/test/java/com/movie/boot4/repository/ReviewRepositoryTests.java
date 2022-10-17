package com.movie.boot4.repository;

import com.movie.boot4.entity.Member;
import com.movie.boot4.entity.Movie;
import com.movie.boot4.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReviews(){

        IntStream.rangeClosed(1,200).forEach(i->{

            //영화 번호에 해당하는 영화에 리뷰어 번호에 해당하는 리뷰어가 리뷰를 작성해야하므로
            //영화번호, 리뷰어번호, 리뷰번호
            //-> 멤버 객체, 영화 객체, 리뷰 객체 생성해 저장
            Long mno=(long)(Math.random() *100)+1;

            Long mid=((long)(Math.random()*100)+1);


            //멤버 객체
            Member member =Member.builder()
                    .mid(mid)
                    .build();

            //리뷰 객체
            Review movieReview=Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random()*5)+1)
                    .text("이 영화에 대한 느낌..."+i)
                    .build();

            reviewRepository.save(movieReview);

        });

    }
}
